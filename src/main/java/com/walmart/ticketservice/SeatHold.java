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

import java.time.Instant;

/**
 *
 * @author gaurav shad
 */
public class SeatHold {
    private static int generateID = 0;
    private int countOfSeats;
    private int holdID;
    private String customerMail;
    private Instant timeStamp;
    
    /**
     * 
     * @param customer the email id of customer
     * @param seats number of seats to hold
     * @param expiry the time instant when the hold will expire
     */
    public SeatHold(String customer, int seats, Instant expiry){
        this.customerMail = customer;
        this.countOfSeats = seats;
        this.timeStamp = expiry;
        this.holdID = generateID++;
    }
    
    public int getCountOfSeats() {
        return this.countOfSeats;
    }

    public int getHoldID() {
        return this.holdID;
    }

    public String getCustomerMail() {
        return this.customerMail;
    }
    
    /**
     * 
     * @return return if current hold is expired or not
     */
    public boolean isExpired(){
        return !this.timeStamp.isAfter(Instant.now());
    }
}
