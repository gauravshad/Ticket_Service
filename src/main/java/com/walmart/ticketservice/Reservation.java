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
    
    /**
     * 
     * @param customer the email id of customer
     * @param seats list of seat numbers reserved
     */
    public Reservation(String customer, List<Integer> seats){
        this.customerMail = customer;
        this.seats = seats;
        counter++;
        StringBuffer code = new StringBuffer();
        
        // generate the unique reservation confirmation code
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
