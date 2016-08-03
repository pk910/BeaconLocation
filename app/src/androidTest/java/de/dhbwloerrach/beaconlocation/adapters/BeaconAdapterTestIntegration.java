package de.dhbwloerrach.beaconlocation.adapters;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;
import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by David on 7/24/15.
 * Implements a testing class for BeaconAdapter class
 */
public class BeaconAdapterTestIntegration extends AndroidTestCase {
    private IntegrationTestHelper helper = new IntegrationTestHelper();
    private BeaconList beacons;
    private BeaconAdapter adapter;

    private DecimalFormat numberFormat = new DecimalFormat("#");

    private static final int COUNT_BEACONS = 10;

    public BeaconAdapterTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Beacon> beacons = helper.createBeacons(COUNT_BEACONS);

        FilterTyp filterTyp = FilterTyp.Minor;

        adapter = new BeaconAdapter(getContext());
        adapter.setFilterTyp(filterTyp);
        adapter.addItems(beacons);

        this.beacons = new BeaconList();
        this.beacons.addAll(beacons);
        this.beacons.sort(filterTyp);
    }

    public void testClearItems() throws Exception {
        adapter.clearItems();
        assertTrue(adapter.getCount() == 0);
    }

    public void testGetCount() throws Exception {
        assertEquals(adapter.getCount(), COUNT_BEACONS);
        assertEquals(adapter.getCount(), beacons.size());
    }

    public void testGetItem() throws Exception {
        for (int index = 0; index < beacons.size(); index++) {
            assertEquals(adapter.getItem(index), beacons.get(index));
        }
    }

    public void testGetPosition() throws Exception {
        for (int index = 0; index < beacons.size(); index++) {
            assertEquals(adapter.getPosition(beacons.get(index)), index);
        }
    }

    public void testGetView() {
        for (int index = 0; index < beacons.size(); index++) {
            View view = adapter.getView(index, null, null);

            TextView viewMinor = (TextView) view.findViewById(R.id.minor);
            TextView viewRssi = (TextView) view.findViewById(R.id.rssi);

            assertNotNull(viewMinor);
            assertNotNull(viewRssi);

            int rssi = beacons.get(index).getRssi();
            String rssiValue = (rssi == 0) ? "-" : numberFormat.format(rssi);

            assertEquals(viewMinor.getText(), beacons.get(index).getMinor().toString());
            assertEquals(viewRssi.getText(), rssiValue);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        beacons.clear();
        adapter = null;
    }
}
