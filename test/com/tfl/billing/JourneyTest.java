package com.tfl.billing;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import junit.framework.TestCase;
import java.text.SimpleDateFormat;

import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

public class JourneyTest extends TestCase{

    Journey testJourney;
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    List<Customer> customers = customerDatabase.getCustomers();
    OysterCardReader oysterCardReader = new OysterCardReader();
    long timeStart = System.currentTimeMillis();
    long timeEnd = System.currentTimeMillis();
    @Override
    protected void setUp() throws Exception
    {
        // TODO Auto-generated method stub
        super.setUp();
        JourneyStart journeyStart = new JourneyStart(customers.get(0).cardId(), oysterCardReader.id());
        timeStart = System.currentTimeMillis();
        //TimeUnit.MINUTES.sleep(1);
        JourneyEnd journeyEnd = new JourneyEnd(customers.get(0).cardId(), oysterCardReader.id());
        timeEnd = System.currentTimeMillis();
        testJourney = new Journey(journeyStart, journeyEnd);
    }

    @Test
    public void testOriginId() {
        assertEquals(oysterCardReader.id(), testJourney.originId());
    }

    @Test
    public void testDestinationId() {
        assertEquals(oysterCardReader.id(), testJourney.destinationId());
    }

    @Test
    public void testFormattedStartTime() {
        assertEquals((SimpleDateFormat.getInstance().format(new Date(timeStart))), testJourney.formattedStartTime());
    }

    @Test
    public void testFormattedEndTime() {
        assertEquals((SimpleDateFormat.getInstance().format(new Date(timeEnd))), testJourney.formattedEndTime());
    }

    @Test
    public void testStartTime() {
        assertEquals(new Date(timeStart), testJourney.startTime());
    }

    @Test
    public void testEndTime() {
        assertEquals(new Date(timeEnd), testJourney.endTime());
    }

    @Test
    public void testDurationSeconds() {
        assertEquals((int) ((timeEnd - timeStart) / 1000), testJourney.durationSeconds());
    }

    @Test
    public void testDurationMinutes() {
        assertEquals("" + (int) ((timeEnd - timeStart) / 1000) / 60 + ":" + (int) ((timeEnd - timeStart) / 1000) % 60, testJourney.durationMinutes());
    }

}
