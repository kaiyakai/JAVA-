package com.tfl.billing;

import java.util.List;
import java.util.UUID;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
//import com.tfl.billing.*;
public class AllTest {
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.tfl.test");
        suite.addTestSuite(JourneyEndTest.class);
        suite.addTestSuite(JourneyStartTest.class);
        suite.addTestSuite(JourneyTest.class);
        suite.addTestSuite(TravelTrackerTest.class);
        return suite;

    }
    public static void main(String[] args) throws Exception {
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = customerDatabase.getCustomers();
        UUID cardId = customers.get(0).cardId();
        OysterCard myCard = new OysterCard(cardId.toString());
        OysterCardReader paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);
        OysterCardReader kingsCrossReader = OysterReaderLocator.atStation(Station.KINGS_CROSS);
        TravelTracker travelTracker = new TravelTracker();
        travelTracker.connect(paddingtonReader, bakerStreetReader, kingsCrossReader);
        paddingtonReader.touch(myCard);
        //minutesPass(5);
        bakerStreetReader.touch(myCard);
        //minutesPass(15);
        bakerStreetReader.touch(myCard);
        //minutesPass(10);
        kingsCrossReader.touch(myCard);
        travelTracker.chargeAccounts();
    }
    private static void minutesPass(int n) throws InterruptedException {
        Thread.sleep(n * 60 * 1000);
    }
}

