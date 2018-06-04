/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public synchronized void book(int n, String customer){
        for(int i=0; i<n; i++){
            counter++;
            seats.set(counter, customer);
        }
    }
}
