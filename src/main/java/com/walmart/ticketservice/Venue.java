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

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gaurav shad
 */
public class Venue {
    private int capacity;
    private int available;
    private int reserved;
    private int onHold;
    private int counter;
    private List<String> seats;
    
    /**
     * 
     * @param capacity to initialize a venue with the given capacity
     */
    public Venue(int capacity){
        this.capacity = capacity;
        this.available = this.capacity;
        this.reserved = 0;
        this.onHold = 0;
        this.counter = 0;
        this.seats = Arrays.asList(new String[this.capacity]);
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getOnHold() {
        return onHold;
    }

    public void setOnHold(int onHold) {
        this.onHold = onHold;
    }

    public List<String> getSeats() {
        return seats;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCounter() {
        return counter;
    }
    
    /**
     * 
     * @param n number of seats to reserve
     * @param customer email id of customer
     * this function will reserve n seats for the customer
     */
    public synchronized void book(int n, String customer){
        for(int i=0; i<n; i++){
            counter++;
            seats.set(counter, customer);
        }
    }
}
