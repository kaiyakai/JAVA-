package com.tfl.billing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.oyster.OysterCardReader;
import com.tfl.billing.JourneyStart;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import junit.framework.TestCase;

public class JourneyStartTest extends TestCase{

    JourneyStart[] testJoStart;
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    List<Customer> customers = customerDatabase.getCustomers();
    OysterCardReader oysterCardReader = new OysterCardReader();
    int customerSize;
    long[] time;

    @Override
    protected void setUp() throws Exception
    {
        // TODO Auto-generated method stub
        super.setUp();
        customerSize = customers.size();
        time = new long[customerSize];
        testJoStart = new JourneyStart[customerSize];
        for(int i=0;i<customerSize;i++){
            testJoStart[i] = new JourneyStart(customers.get(i).cardId(), oysterCardReader.id());
            time[i] = System.currentTimeMillis();
        }
    }

    @Test
    public void testCardId() {
        for(int i=0;i<customerSize;i++){
            assertEquals(customers.get(i).cardId(), testJoStart[i].cardId());
        }
    }

    @Test
    public void testReaderId() {
        for(int i=0;i<customerSize;i++){
            assertEquals(oysterCardReader.id(), testJoStart[i].readerId());
        }
    }

    @Test
    public void testTime() {
        for(int i=0;i<customerSize;i++){
            assertEquals(time[i], testJoStart[i].time());
        }
    }

}
