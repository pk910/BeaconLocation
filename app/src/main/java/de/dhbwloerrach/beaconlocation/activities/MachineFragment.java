package de.dhbwloerrach.beaconlocation.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.BeaconSettings;
import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.extensions.ExtensionInterface;
import de.dhbwloerrach.beaconlocation.extensions.ToastExtension;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.Machine;
import de.dhbwloerrach.beaconlocation.models.RssiAverageType;

/**
 * Created by Lukas on 05.08.2015.
 * Implements a fragment that shows a single machine
 */
public class MachineFragment extends BaseFragment implements IBeaconListView {
    private BeaconAdapter adapter;
    private Boolean updatePaused = false;
    private final ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private Menu menu;
    private Machine machine;
    private Thread glassAdpaterThread;
    private ExtensionInterface extension;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();

        activity.getCommons().startMonitoring(this);
        machine = getArguments().getParcelable("machine");

        DatabaseHandler databaseHandler = new DatabaseHandler(activity);
        updateBeaconListView(databaseHandler.getAllBeaconsByMachine(machine.getId()));
        databaseHandler.close();

        TextView textView = (TextView) activity.findViewById(R.id.name);
        textView.setText(machine.getName());

        ListView listView = (ListView) activity.findViewById(R.id.beaconList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatePaused = true;
                ListView listView = (ListView) parent;
                Beacon beacon = (Beacon) listView.getAdapter().getItem(position);

                if (selectedBeacons.contains(beacon)) {
                    selectedBeacons.remove(beacon);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if (selectedBeacons.isEmpty()) {
                        updatePaused = false;
                    }
                } else {
                    selectedBeacons.add(beacon);
                    view.setBackgroundColor(0xFF8db6cd);
                }

                updateMenuButtons();
            }
        });

        listView.setAdapter(adapter);
        listView.setEmptyView(activity.findViewById(R.id.emptyList_machine));

        extension = new ToastExtension();
        extension.connect(this.getActivity());
        glassAdpaterThread=new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted())
                {
                    extension.sendMessage("Looking at machine \"" + machine.getName()+"\"");
                    try {
                        Thread.sleep(5000);
                        if(Thread.currentThread().isInterrupted())
                        {
                            break;
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        glassAdpaterThread.start();
    }

    /**
     * Initialize the fragment
     */
    private void initialize() {
        if(!initialized) {
            adapter = new BeaconAdapter(activity);
            initialized = true;
        }
    }

    /**
     * Change the menu item title
     */
    private void updateMenuButtons() {
        menu.findItem(R.id.add_beacon).setVisible(selectedBeacons.isEmpty());
        menu.findItem(R.id.delete_machine).setVisible(selectedBeacons.isEmpty());
        menu.findItem(R.id.delete_beacon).setVisible(!selectedBeacons.isEmpty());
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        initialize();
        this.menu = menu;

        activity.getMenuInflater().inflate(R.menu.menu_machine, menu);
        setRSSIMode(RssiAverageType.None);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        DatabaseHandler databaseHandler = new DatabaseHandler(activity);

        try {
            switch (itemId){
                case R.id.add_beacon:
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("oldMachine", machine);
                    activity.getCommons().switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH, bundle);
                    return true;

                case R.id.delete_machine:
                    menuActionDeleteMachine(databaseHandler);
                    return true;

                case R.id.delete_beacon:
                    menuActionDeleteBeacon(databaseHandler);
                    return true;
                case R.id.rssi_average:
                    switch (adapter.getRssiAverageType()) {
                        case None:
                            setRSSIMode(RssiAverageType.Average);
                            break;
                        case Average:
                            setRSSIMode(RssiAverageType.SmoothedAverage);
                            break;
                        case SmoothedAverage:
                            setRSSIMode(RssiAverageType.None);
                            break;
                        default:
                            return false;
                    }
                    break;
                default:
                    Log.e("Error","unkown ItemID");
            }

            return false;
        } finally {
            databaseHandler.close();
        }
    }

    /**
     * Chang ethe rssi calculation mode
     * @param rssiAverageType RssiAverageType
     */
    private void setRSSIMode(RssiAverageType rssiAverageType){
        switch (rssiAverageType) {
            case None:
                menu.findItem(R.id.rssi_average).setTitle(R.string.modusRssiNormal);
                break;
            case Average:
                menu.findItem(R.id.rssi_average).setTitle(R.string.modusRssiAvg);
                break;
            case SmoothedAverage:
                menu.findItem(R.id.rssi_average).setTitle(R.string.modusRssiAdv);
                break;
            default:
        }
        adapter.setRssiAverageType(rssiAverageType);
    }

    @Override
    public void refreshList(ArrayList<Beacon> beacons) {
        if(updatePaused) {
            return;
        }

        BeaconList visibleBeacons = new BeaconList();
        visibleBeacons.addAll(beacons);
        BeaconList filteredBeacons = visibleBeacons.filterByLast(5);

        final BeaconList beaconList = new BeaconList();
        DatabaseHandler databaseHandler = new DatabaseHandler(activity);
        ArrayList<Beacon> machineBeacons = databaseHandler.getAllBeaconsByMachine(machine.getId());

        try {
            //add visible Beacons, which are part of the machine
            for (Beacon beacon : filteredBeacons) {
                if (machineBeacons.contains(beacon)) {
                    beaconList.add(beacon);
                }
            }

            //add Beacons, which are part of the machine and not visible
            for (Beacon beacon : machineBeacons) {
                if (!beaconList.contains(beacon)) {
                    beaconList.add(beacon);
                }
            }
        } finally {
            databaseHandler.close();
        }

        updateBeaconListView(beaconList);

        boolean allInRange = beaconList.size() > 0;
        for (Beacon beacon : beaconList) {
            double rssi = beacon.getRssiByAverageType(adapter.getRssiAverageType(), BeaconSettings.RSSI_AVERAGE_SECONDS);
            if(beacon.getRssiDistanceStatus(rssi) != Beacon.RssiDistanceStatus.IN_RANGE) {
                allInRange = false;
                break;
            }
        }

        final boolean finalAllInRange = allInRange;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView rangeView = (TextView) activity.findViewById(R.id.allBeaconsInRange);
                if(rangeView!=null) {
                    rangeView.setVisibility(finalAllInRange ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    /**
     * Notify the beacon adapter for refresh
     * @param beacons ArrayList
     */
    private void updateBeaconListView(final ArrayList<Beacon> beacons) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clearItems();
                adapter.addItems(beacons);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Delete the beacon from machine and database
     * @param databaseHandler DatabaseHandler
     */
    private void menuActionDeleteBeacon(final DatabaseHandler databaseHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.alert_title_deleteBeacon);

        StringBuilder stBuilder= new StringBuilder();
        for (Beacon beacon : selectedBeacons) {
            stBuilder.append(beacon.getMinor().toString());
            if(selectedBeacons.indexOf(beacon) < selectedBeacons.size() - 1) {
                stBuilder.append(", ");
            }
        }
        String beacons = stBuilder.toString();

        builder.setMessage(String.format(activity.getResources().getString(R.string.alert_message_deleteBeacon), beacons));
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        });
        builder.setPositiveButton(R.string.deleteBeacon, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (Beacon beacon : new ArrayList<>(selectedBeacons)) {
                    selectedBeacons.remove(beacon);
                    databaseHandler.deleteBeacon(beacon);
                }
                // DatabaseHandler databaseHandler = new DatabaseHandler(activity);
                updateBeaconListView(databaseHandler.getAllBeaconsByMachine(machine.getId()));
                databaseHandler.close();
                updateMenuButtons();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Delete a machine and all beacons from database
     * @param databaseHandler DatabaseHandler
     */
    private void menuActionDeleteMachine(final DatabaseHandler databaseHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.alert_title_deleteMachine);
        builder.setMessage(R.string.alert_message_deleteMachine);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        });

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (Beacon beacon : databaseHandler.getAllBeaconsByMachine(machine.getId())) {
                    databaseHandler.deleteBeacon(beacon);
                }
                databaseHandler.deleteMachine(machine);
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.MACHINES_VIEW);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void disconnectView() {
        activity.getCommons().stopMonitoring(this);
        selectedBeacons.clear();

        updatePaused = false;
        adapter.clear();

        glassAdpaterThread.interrupt();
        extension.disconnect();
    }
}
