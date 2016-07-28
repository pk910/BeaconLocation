package de.dhbwloerrach.beaconlocation.models;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by enio on 7/29/16.
 */
public class BeaconTest {

    @org.junit.Test
    public void testGetUuid() throws Exception {
        Beacon testbeacon = new Beacon();
        String uuid = "asdf";
        testbeacon.setUuid(uuid);
        assertThat(testbeacon.getUuid(), is(uuid));
    }

    @org.junit.Test
    public void testSetUuid() throws Exception {
        Beacon testbeacon = new Beacon();
        String uuid = "asdf";
        testbeacon.setUuid(uuid);
        assert(testbeacon.getUuid().equals(uuid));
    }
}