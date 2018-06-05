/*
 * Copyright 2018 gaurav shad.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.walmart.ticketservice;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gaurav shad
 */
public class VenueTicketBookingService implements TicketService{
    private Venue venue;
    private final Duration holdDuration;
    private HashMap<Integer, SeatHold> onHold;
    private HashMap<String, Reservation> booking;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    /**
     * 
     * @param capacity to initialize the venue with given capacity
     * @param duration set the hold duration
     */
    public VenueTicketBookingService(int capacity, Duration duration){
        venue = new Venue(capacity);
        this.holdDuration = duration;
        onHold = new HashMap<Integer, SeatHold>();
        booking = new HashMap<String, Reservation>();
    }
    
    /**
     * this function will remove that are expired at the current time Instant
     */
    private synchronized void removeExpiredHolds(){
        int sum = 0;
        Set<Integer> toRemove = new HashSet<Integer>();
        synchronized(onHold){
            Set<Integer> keys = onHold.keySet();

            for(int id:keys){
                if(onHold.get(id).isExpired())
                    toRemove.add(id);
                else
                    sum = sum + onHold.get(id).getCountOfSeats();
            }
            
            for(int k:toRemove)
                onHold.remove(k);
        }
        
        synchronized(venue){
            venue.setOnHold(sum);
            venue.setAvailable(venue.getCapacity() - venue.getReserved() - venue.getOnHold());
        }
    }
    
    /**
     * 
     * @return the number of seats available at the venue at the moment
     */
    public synchronized int numSeatsAvailable(){
        this.removeExpiredHolds();
        synchronized(venue){
            return venue.getAvailable();
        }
    }
    
    /**
     * 
     * @param emailStr email id to validate
     * @return whether the given email is valid or not
     */
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
}
    /**
     * 
     * @param numSeats number of seats to hold
     * @param customerEmail customer email id
     * @return null if enough seats are not available else seat hold object
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail){
        if(!validate(customerEmail))
            throw new IllegalArgumentException("Invalid Email address");
        
        if(numSeats<1)
            throw new IllegalArgumentException("Invalid number of seats");
        
        int available = this.numSeatsAvailable();
        
        if(numSeats < available){
            SeatHold seathold = new SeatHold(customerEmail, numSeats, Instant.now().plus(holdDuration));
            synchronized(onHold){
                onHold.put(seathold.getHoldID(), seathold);
            }
            synchronized(venue){
                venue.setOnHold(venue.getOnHold() + numSeats);
                venue.setAvailable(venue.getAvailable() - numSeats);
            }
            
            return seathold;
        }
        else
            return null;
    }
    
    /**
     * 
     * @param seatHoldId Id given by previous function to confirm the hold
     * @param customerEmail email used to hold tickets
     * @return null if the tickets are not reserved else reservation confirmation code
     */
    public synchronized String reserveSeats(int seatHoldId, String customerEmail){
        SeatHold hold;
        int start, end;
        synchronized(onHold){
            if(!onHold.containsKey(seatHoldId))
                return null;

            if(onHold.get(seatHoldId).isExpired()){
                venue.setOnHold(venue.getOnHold() - onHold.get(seatHoldId).getCountOfSeats());
                venue.setAvailable(venue.getAvailable() + onHold.get(seatHoldId).getCountOfSeats());

                onHold.remove(seatHoldId);

                return null;
            }

            if(!onHold.get(seatHoldId).getCustomerMail().equals(customerEmail))
                throw new IllegalArgumentException("Invalid seatHoldId or customerEmail");

            hold = onHold.get(seatHoldId);
            start = venue.getCounter()+1;
            venue.book(hold.getCountOfSeats(), hold.getCustomerMail());
            end = venue.getCounter();
            onHold.remove(seatHoldId);
        }
        List<Integer> seats = new ArrayList<Integer>();
        
        for(int i=start; i<=end; i++)
            seats.add(i);
        
        Reservation reservation = new Reservation(customerEmail, seats);
        
        booking.put(reservation.getReserveCode(), reservation);
        
        synchronized(venue){
            venue.setOnHold(venue.getOnHold() - hold.getCountOfSeats());
            venue.setReserved(venue.getReserved() + hold.getCountOfSeats());
        }
        
        return reservation.getReserveCode();
    }
    
    /**
     * 
     * @param reserveId confirmation code
     * @return list of seat numbers reserved against that reservation id
     */
    public synchronized List<Integer> getReservedSeats(String reserveId){
        if(booking.containsKey(reserveId))
            return booking.get(reserveId).getSeats();
        else
            throw new IllegalArgumentException("Invalid reservation code");
    }
    
    
}
