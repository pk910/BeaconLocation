package de.dhbwloerrach.beaconlocation.models;

import android.test.AndroidTestCase;

import java.util.List;

import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by David on 7/24/15.
 * Implements a testing class for the BeaconList class
 */
public class BeaconListTestIntegration extends AndroidTestCase {
    private final IntegrationTestHelper helper = new IntegrationTestHelper();

    private BeaconList beacons;

    private static final int COUNT_BEACONS = 10;

    public BeaconListTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.beacons = new BeaconList();

        List<Beacon> beacons = helper.createBeacons(COUNT_BEACONS);
        for(Beacon beacon : beacons) {
            beacon.setRssi(helper.createRandom(-150, 0));
            beacon.setDistance(helper.createRandom(0, 20));
            Thread.sleep(567);
        }

        this.beacons.addAll(beacons);
    }

    public void testFilterByLast() throws Exception {
        BeaconList filteredList = beacons.filterByLast(2);
        assertEquals(3, filteredList.size());
    }

    public void testSortByRssi() throws Exception {
        beacons.sort(FilterTyp.RSSI);
        for(int i = 1; i < beacons.size(); i++){
            assertTrue(beacons.get(i).getRssi() <= beacons.get(i-1).getRssi());
        }
    }

    public void testSortByMinor() throws Exception {
        beacons.sort(FilterTyp.Minor);
        for(int i = 1; i < beacons.size(); i++){
            assertTrue(beacons.get(i).getMinor() >= beacons.get(i-1).getMinor());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
