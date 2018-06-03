/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public boolean isExpired(){
        return !this.timeStamp.isAfter(Instant.now());
    }
}
