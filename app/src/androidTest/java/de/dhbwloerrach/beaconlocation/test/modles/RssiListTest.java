package de.dhbwloerrach.beaconlocation.test.modles;

import android.test.AndroidTestCase;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.models.DistanceList;
import de.dhbwloerrach.beaconlocation.models.RssiList;
import de.dhbwloerrach.beaconlocation.models.TimedRssi;
import de.dhbwloerrach.beaconlocation.test.helpers.TestHelper;

/**
 * Created by Lukas on 11.09.2015.
 */
public class RssiListTest extends AndroidTestCase {
    private TestHelper helper = new TestHelper();
    private RssiList rssiList;

    private ArrayList<Integer> rssis;

    public RssiListTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        int count = 10;

        rssis = new ArrayList<>(count);
        rssiList = new RssiList();
        for (int index = 0; index < count; index++) {
            int rssi = index;
            rssis.add(rssi);
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
