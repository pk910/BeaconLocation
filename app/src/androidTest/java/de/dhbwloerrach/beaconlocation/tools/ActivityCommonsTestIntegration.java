package de.dhbwloerrach.beaconlocation.tools;

import android.os.Bundle;
import android.test.AndroidTestCase;

import java.util.AbstractMap;
import java.util.Map;

import de.dhbwloerrach.beaconlocation.activities.ActivityCommons;

public class ActivityCommonsTestIntegration extends AndroidTestCase {

    // --Commented out by Inspection (8/3/16 10:25 PM):private IntegrationTestHelper helper = new IntegrationTestHelper();
    private final ActivityCommons commons;

    private static final int COUNT_FRAGMENTS = 10;

    @SuppressWarnings("unused")
    public ActivityCommonsTestIntegration() {
        super();
        commons = new ActivityCommons(null);
    }

    @SuppressWarnings("EmptyMethod") // Leave in for extending.
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SuppressWarnings("unused")
    public void testFragmentStack() throws Exception {
        for (int index = 0; index < COUNT_FRAGMENTS; index++) {
            Bundle bundle = null;
            if (index % 2 == 0) {
                bundle = new Bundle();
                bundle.putInt("index", index);
            }

            commons.getFragmentStack().add(new AbstractMap.SimpleEntry<>(ActivityCommons.FragmentType.ADD_MACHINE, bundle));
        }

        assertEquals(commons.getFragmentStack().size(), COUNT_FRAGMENTS);
        for (int index = 0; index < COUNT_FRAGMENTS; index++) {
            {
                Map.Entry<ActivityCommons.FragmentType, Bundle> entry = commons.getFragmentStack().remove(commons.getFragmentStack().size() - 1);
                if (index % 2 == 1) {
                    assertNotNull(entry.getValue());
                } else {
                    assertNull(entry.getValue());
                }
            }
        }
    }

    @SuppressWarnings("EmptyMethod") // Leave in for extending.
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
