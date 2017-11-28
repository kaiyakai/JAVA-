package com.tfl.billing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oyster.OysterCardReader;
import com.tfl.billing.Journey;
import com.tfl.billing.JourneyEnd;
import com.tfl.billing.JourneyStart;
import com.tfl.billing.TravelTracker;
import com.tfl.billing.UnknownOysterCardException;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;


import junit.framework.TestCase;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.UUID;
import java.lang.reflect.Method;

public class TravelTrackerTest extends TestCase{

    static final BigDecimal test_offpeak = new BigDecimal(2.40);
    static final BigDecimal test_peak = new BigDecimal(3.20);
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    List<Customer> customers = customerDatabase.getCustomers();
    OysterCardReader[] oysterCardReader;
    JourneyStart[] testJoStart;
    JourneyEnd[] testJoEnd;
    Journey[] testJourney;
    int customerSize;
    long[] Starttime;
    long[] Endtime;
    TravelTracker travelTracker;
    Method method;

    @Override
    protected void setUp() throws Exception
    {
        // TODO Auto-generated method stub
        super.setUp();
        travelTracker = new TravelTracker();
        customerSize = customers.size();
        Starttime = new long[customerSize];
        Endtime = new long[customerSize];
        testJoStart = new JourneyStart[customerSize];
        testJoEnd = new JourneyEnd[customerSize];
        testJourney = new Journey[customerSize];
        oysterCardReader = new OysterCardReader[2*customerSize];
        for(int i=0;i<customerSize;i++){
            oysterCardReader[2*i] = new OysterCardReader();
            testJoStart[i] = new JourneyStart(customers.get(i).cardId(), oysterCardReader[2*i].id());
            Starttime[i] =  System.currentTimeMillis();
            /*int min = 1, max = 5;
            Random random = new Random();
            int s = random.nextInt(max) % (max-min+1) + min;
            TimeUnit.MINUTES.sleep(s);*/
            oysterCardReader[2*i+1] = new OysterCardReader();
            testJoEnd[i] = new JourneyEnd(customers.get(i).cardId(), oysterCardReader[2*i+1].id());
            Endtime[i] =  System.currentTimeMillis();
            testJourney[i] = new Journey(testJoStart[i], testJoEnd[i]);
        }
    }

    @Test
    public void testPeak() {
        Object isPeak = null;
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        try{
            method = travelTracker.getClass().getDeclaredMethod("peak",Date.class);
            method.setAccessible(true);
            isPeak = method.invoke(travelTracker, date);
        }catch(Exception e){
            System.out.println(e);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if((hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 19)){
            assertEquals("true",isPeak.toString());
        }
        else{
            assertEquals("false",isPeak.toString());
        }
    }

    @Test(expected = UnknownOysterCardException.class)
    public void testCardScanned() {
        OysterCardReader oysterCardReader = new OysterCardReader();
        try{
            travelTracker.cardScanned(UUID.randomUUID(), oysterCardReader.id());
            fail("No exception thrown.");
        }catch(Exception ex){
            assertTrue(ex.getMessage().contains("Oyster Card does not correspond to a known customer. Id: "));
        }
    }

}
