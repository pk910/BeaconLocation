package de.dhbwloerrach.beaconlocation.modles;

import android.test.AndroidTestCase;

import java.util.List;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by David on 7/24/15.
 */
public class BeaconTestIntegration extends AndroidTestCase {
    private IntegrationTestHelper helper = new IntegrationTestHelper();

    private List<Beacon> beacons;

    private static final int COUNT_BEACONS = 10;

    public BeaconTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Beacon> beacons = helper.createBeacons(COUNT_BEACONS);
        for(Beacon beacon : beacons) {
            beacon.setRssi(helper.createRandom(-150, 0));
        }

        this.beacons = beacons;
    }

    public void testDistanceStatus() {
        for (Beacon beacon : beacons) {
            Beacon.RssiDistanceStatus status = beacon.getRssiDistanceStatus(beacon.getRssi());

            if (beacon.getRssi() >= -70 && beacon.getRssi() < 0) {
                assertEquals(Beacon.RssiDistanceStatus.IN_RANGE, status);
            } else if (beacon.getRssi() < -70 && beacon.getRssi() >= -80) {
                assertEquals(Beacon.RssiDistanceStatus.NEAR_BY_RANGE, status);
            } else if (beacon.getRssi() < -80 && beacon.getRssi() >= -85) {
                assertEquals(Beacon.RssiDistanceStatus.AWAY, status);
            } else if (beacon.getRssi() < -85 && beacon.getRssi() >= -99) {
                assertEquals(Beacon.RssiDistanceStatus.FAR_AWAY, status);
            } else {
                assertEquals(Beacon.RssiDistanceStatus.UNKNOWN, status);
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
