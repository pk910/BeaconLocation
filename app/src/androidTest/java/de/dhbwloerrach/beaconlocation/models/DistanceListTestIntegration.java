package de.dhbwloerrach.beaconlocation.models;

import android.test.AndroidTestCase;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by David on 7/29/15.
 * Implements a testing class for the distance list implementation
 */
public class DistanceListTestIntegration extends AndroidTestCase {
    private final IntegrationTestHelper helper = new IntegrationTestHelper();
    private DistanceList distanceList;

    private ArrayList<Double> distances;

    public DistanceListTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        int count = helper.createRandom(8, 15);

        distances = new ArrayList<>(count);
        distanceList = new DistanceList();
        for (int index = 0; index < count; index++) {
            double distance = helper.createRandom(1d, 5d);
            distances.add(distance);
            distanceList.add(new TimedDistance(distance));
            Thread.sleep(567);
        }
    }

    public void testGetLast() throws Exception {
        DistanceList lastDistances = distanceList.getLast(2);
        assertEquals(3, lastDistances.size());
    }

    public void testAverageDistance() throws Exception {
        double sum = 0;
        for(Double distance : distances)
            sum += distance;

        assertEquals(distanceList.getAverageDistance(), sum / distances.size());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        distanceList.clear();
        distanceList = null;
    }
}
