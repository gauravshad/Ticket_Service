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

import com.walmart.ticketservice.SeatHold;
import com.walmart.ticketservice.VenueTicketBookingService;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gaurav shad
 */
public class VenueTicketBookingServiceTest {
    VenueTicketBookingService service;
    
    public VenueTicketBookingServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * to initialize a service used for testing
     */
    @Before
    public void setUp() {
        service = new VenueTicketBookingService(50, Duration.ofSeconds(5));
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * to test the numSeatsAvailable functionality
     */
    @Test
    public void numSeatsAvailableTest(){
        System.out.println("    .Running numSeatsAvailableTest");
        assert(service.numSeatsAvailable() == 50);
    }
    
    /**
     * to test findAndHoldSeats functionality
     */
    @Test
    public void findAndHoldSeatsTest(){
        System.out.println("    .Running findAndHoldSeatsTest");
        SeatHold hold1 = service.findAndHoldSeats(5, "gs@mail.com");
        assertNotNull(hold1);
        assert(service.numSeatsAvailable() == 45);
        
        SeatHold hold2 = service.findAndHoldSeats(10, "abc@abc.com");
        assertNotNull(hold2);
        
        assert(service.numSeatsAvailable() == 35);
        
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VenueTicketBookingServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assert(service.numSeatsAvailable() == 50);
        
        SeatHold hold3 = service.findAndHoldSeats(50, "gs@gmail.com");
        assertNull(hold3);
        
        SeatHold hold4;
        
        try{
            hold4 = service.findAndHoldSeats(-1, "mail@mail.com");
        }
        catch(Exception e){
            assert(e.getMessage().equals("Invalid number of seats"));
        }
        
        try{
            hold4 = service.findAndHoldSeats(13, "mailcom");
        }
        catch(Exception e){
            assert(e.getMessage().equals("Invalid Email address"));
        }
    }
    
    /**
     * to test reserveSeats functionality
     */
    @Test
    public void reserveSeatsTest(){
        System.out.println("    .Running reserveSeatsTest");
        SeatHold hold = service.findAndHoldSeats(5, "gs@mail.com");
        assertNotNull(hold);
        
        String code = service.reserveSeats(hold.getHoldID(), "gs@mail.com");
        assertNotNull(code);
        
        String code2 = service.reserveSeats(6554, "gs@mail.com");
        assertNull(code2);
        
        SeatHold hold2 = service.findAndHoldSeats(2, "rt@aol.com");
        assertNotNull(hold2);
        
        try{
        String code3 = service.reserveSeats(hold2.getHoldID(), "r@aol.com");
        }
        catch(Exception e){
            assert(e.getMessage().equals("Invalid seatHoldId or customerEmail"));
        }
        
    }
    
    /**
     * to test getReservedSeats functionality
     */
    @Test
    public void getReservedSeatsTest(){
        System.out.println("    .Running getReservedSeatsTest");
        SeatHold hold = service.findAndHoldSeats(2, "rt@mail.com");
        assertNotNull(hold);
        
        String code = service.reserveSeats(hold.getHoldID(), "rt@mail.com");
        assertNotNull(code);
        
        List<Integer> res = service.getReservedSeats(code);
        assertNotNull(res);
        
        try{
        List<Integer> res2 = service.getReservedSeats("failtest");
        }
        catch(Exception e){
            assert(e.getMessage().equals("Invalid reservation code"));
        }
    }
    
}
