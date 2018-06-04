/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.walmart.ticketservice;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public VenueTicketBookingService(int capacity, Duration duration){
        venue = new Venue(capacity);
        this.holdDuration = duration;
        onHold = new HashMap<Integer, SeatHold>();
        booking = new HashMap<String, Reservation>();
    }
    
    private synchronized void removeExpiredHolds(){
        int sum = 0;
        Set<Integer> keys = onHold.keySet();
        
        for(int id:keys){
            if(onHold.get(id).isExpired())
                onHold.remove(id);
            else
                sum = sum + onHold.get(id).getCountOfSeats();
        }
        
        venue.setOnHold(sum);
        venue.setAvailable(venue.getCapacity() - venue.getReserved() - venue.getOnHold());
    }
    
    
    public synchronized int numSeatsAvailable(){
        this.removeExpiredHolds();
        return venue.getAvailable();
    }
    
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
}
    
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail){
        if(!validate(customerEmail))
            throw new IllegalArgumentException("Invalid Email address");
        
        if(numSeats<1)
            throw new IllegalArgumentException("Invalid number of seats");
        
        int available = this.numSeatsAvailable();
        
        if(numSeats < available){
            SeatHold seathold = new SeatHold(customerEmail, numSeats, Instant.now().plus(holdDuration));
            onHold.put(seathold.getHoldID(), seathold);
            venue.setOnHold(venue.getOnHold() + numSeats);
            venue.setAvailable(venue.getAvailable() - numSeats);
            
            return seathold;
        }
        else
            return null;
    }
    
    public synchronized String reserveSeats(int seatHoldId, String customerEmail){
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
        
        SeatHold hold = onHold.get(seatHoldId);
        int start = venue.getCounter()+1;
        venue.book(hold.getCountOfSeats(), hold.getCustomerMail());
        int end = venue.getCounter();
        onHold.remove(seatHoldId);
        
        List<Integer> seats = new ArrayList<Integer>();
        
        for(int i=start; i<=end; i++)
            seats.add(i);
        
        Reservation reservation = new Reservation(customerEmail, seats);
        
        booking.put(reservation.getReserveCode(), reservation);
        
        venue.setOnHold(venue.getOnHold() - hold.getCountOfSeats());
        venue.setReserved(venue.getReserved() + hold.getCountOfSeats());
        
        return reservation.getReserveCode();
    }
    
    public synchronized List<Integer> getReservedSeats(String reserveId){
        if(booking.containsKey(reserveId))
            return booking.get(reserveId).getSeats();
        else
            throw new IllegalArgumentException("Invalid reservation code");
    }
    
    
}
