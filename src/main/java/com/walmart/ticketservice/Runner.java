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
import java.util.Scanner;

/**
 *
 * @author gaurav shad
 */
public class Runner {
    
    /**
     * display the options on the screen
     */
    private static void populateOptions(){
        System.out.println("1. Check available seats \n2. Hold seats \n3. Reserve seats \n4. Get reservation details \n5. Help [show commands] \n6. Exit");
    }
    
    public static void main(String[] args){
        int capacity = 0;
        int duration = 0;
        int in = 0;
        boolean choice = true;
        
        if(args.length < 2){
            System.out.println("Insufficient arguments. Please try to run again with capacity and duration!");
            System.exit(0);
        }
        
        try{
            capacity = Integer.parseInt(args[0]);
            duration = Integer.parseInt(args[1]);
        }
        catch(Exception e){
            System.out.println("Invalid arguments. Please try to run again with Integral values!");
            System.exit(0);
        }
        
        VenueTicketBookingService service = new VenueTicketBookingService(capacity, Duration.ofSeconds(duration));
        Scanner input = new Scanner(System.in);
        
        System.out.println("Ticket Booking Service [v-1.0] [Capacity = " + capacity + " Hold duration = " + duration + "]" );
        populateOptions();
        
        while(choice){
            
            System.out.print("\nEnter an option: ");
            try{
                in = Integer.parseInt(input.next());
            }
            catch(Exception e){
                System.out.println("Invalid input. Please try again!");
                continue;
            }
            
            switch(in){
                case 1: System.out.println("Seats available = " + service.numSeatsAvailable());
                        break;
                
                case 2: int n;
                        String Email;
                        System.out.print("Enter No. of seats to hold: ");
                        try{
                            n = Integer.parseInt(input.next());
                        }
                        catch(Exception e){
                            System.out.println("Invalid input. Please try again!");
                            continue;
                        }
                        
                        System.out.print("Enter Email Id: ");
                        Email = input.next();
                        
                        SeatHold hold;
                        try{
                        hold = service.findAndHoldSeats(n, Email);
                        }
                        catch(Exception e){
                            System.out.print(e.getMessage());
                            System.out.println("Please try again!");
                            break;
                        }
                        
                        if(hold!=null)
                            System.out.println("Seats successfully held. Your hold ID is " + hold.getHoldID());
                        else{
                            System.out.println("Insufficient availability. Please try again in some time!");
                            break;
                        }
                        
                        break;
                        
                case 3: int holdId;
                        String Mail;
                        
                        System.out.print("Enter hold ID: ");
                        try{
                            holdId = Integer.parseInt(input.next());
                        }
                        catch(Exception e){
                            System.out.println("Invalid input. Please try again!");
                            break;
                        }
                        
                        System.out.print("Enter your Email: ");
                        Mail = input.next();
                        String code;
                        
                        try{
                            code = service.reserveSeats(holdId, Mail);
                        }
                        catch(Exception e){
                            System.out.println(e.getMessage());
                            System.out.println("Please try again!");
                            break;
                        }
                        
                        if(code!=null){
                            System.out.println("Seats successfully reserved. Your reservation code is " + code);
                        }
                        else{
                            System.out.println("Hold expired. Please try again!");
                            break;
                        }
                        
                        break;
                       
                case 4: String rcode;
                        System.out.print("Enter reservation code: ");
                        rcode = input.next();
                        
                        System.out.println();
                        
                        try{
                            System.out.println("You have the following seats reserved: " + service.getReservedSeats(rcode));
                        }
                        catch(Exception e){
                            System.out.println(e.getMessage());
                            System.out.println("Please try again!");
                            break;
                        }
                        
                        break;
                        
                case 5: populateOptions();
                        break;
                        
                case 6: System.out.println("Thank you for using our ticket booking service");
                        System.out.println("Shutting down...");
                        try{
                        Thread.sleep(2000);
                        }
                        catch(Exception e){
                        }
                        
                        choice = false;
                        break;
                        
                default: System.out.println("Please enter a valid choice!");
                         break;
            }
        }
    }
   
}
