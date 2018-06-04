/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.walmart.ticketservice;

import java.util.List;

/**
 *
 * @author gaurav shad
 */
public class Reservation {
    private static int counter = 0;
    private String reserveCode;
    private List<Integer> seats;
    private String customerMail;
    
    public Reservation(String customer, List<Integer> seats){
        this.customerMail = customer;
        this.seats = seats;
        counter++;
        StringBuffer code = new StringBuffer();
        code.append(counter)
                .append("-")
                .append(seats.size())
                .append("-")
                .append(customer.split("@")[0]);
        
        this.reserveCode = code.toString();
        
    }

    public String getReserveCode() {
        return reserveCode;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public String getCustomerMail() {
        return customerMail;
    }
    
    
}
