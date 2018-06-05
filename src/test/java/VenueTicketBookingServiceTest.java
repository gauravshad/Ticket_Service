/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    @Before
    public void setUp() {
        service = new VenueTicketBookingService(50, Duration.ofSeconds(5));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void numSeatsAvailableTest(){
        assert(service.numSeatsAvailable() == 50);
    }
    
    @Test
    public void findAndHoldSeatsTest(){
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
    
    @Test
    public void reserveSeatsTest(){
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
    
    @Test
    public void getReservedSeatsTest(){
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
