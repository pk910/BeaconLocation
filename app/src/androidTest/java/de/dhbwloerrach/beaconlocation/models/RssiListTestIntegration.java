package de.dhbwloerrach.beaconlocation.models;

import android.test.AndroidTestCase;

import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by Lukas on 11.09.2015.
 * Implements a test class for RssiList
 */
public class RssiListTestIntegration extends AndroidTestCase {
    @SuppressWarnings("unused")
    private IntegrationTestHelper helper = new IntegrationTestHelper();
    private RssiList rssiList;

    public RssiListTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        int count = 10;

        rssiList = new RssiList();
        for (int rssi = 0; rssi < count; rssi++) {
            rssiList.add(new TimedRssi(rssi));
            Thread.sleep(567);
        }
    }

    public void testGetLast() throws Exception {
        RssiList lastRssis = rssiList.getLast(2);
        assertEquals(3, lastRssis.size());
    }

    public void testGetAverage() throws Exception {
        double average = rssiList.getAverage();
        assertEquals(4.5, average);
    }

    public void testGetSmoothAverage() throws Exception {
        double average = rssiList.getSmoothAverage();
        assertEquals(4.5, average);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        rssiList.clear();
        rssiList = null;
    }
}
