package de.dhbwloerrach.beaconlocation.adapters;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.Machine;
import de.dhbwloerrach.beaconlocation.helpers.IntegrationTestHelper;

/**
 * Created by David on 7/24/15.
 * Implements a testing class for machine adapter
 */
public class MachineAdapterTestIntegration extends AndroidTestCase {
    private final IntegrationTestHelper helper = new IntegrationTestHelper();
    private List<Machine> machines;
    private MachineAdapter adapter;

    private static final int COUNT_MACHINES = 10;

    public MachineAdapterTestIntegration() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Machine> machines = helper.createMachines(COUNT_MACHINES);

        adapter = new MachineAdapter(getContext());
        adapter.addItems(machines);

        this.machines = new ArrayList<>();
        this.machines.addAll(machines);
    }

    public void testClearItems() throws Exception {
        adapter.clearItems();
        assertTrue(adapter.getCount() == 0);
    }

    public void testGetCount() throws Exception {
        assertEquals(adapter.getCount(), COUNT_MACHINES);
        assertEquals(adapter.getCount(), machines.size());
    }

    public void testGetItem() throws Exception {
        for (int index = 0; index < machines.size(); index++) {
            assertEquals(adapter.getItem(index), machines.get(index));
        }
    }

    public void testGetPosition() throws Exception {
        for (int index = 0; index < machines.size(); index++) {
            assertEquals(adapter.getPosition(machines.get(index)), index);
        }
    }

    public void testGetView() {
        for (int index = 0; index < machines.size(); index++) {
            View view = adapter.getView(index, null, null);

            TextView machineName = (TextView) view.findViewById(R.id.machineName);

            assertNotNull(machineName);

            assertEquals(machineName.getText(), machines.get(index).getName().toString());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        machines.clear();
        adapter = null;
    }
}