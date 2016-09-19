package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.BeaconSettings;
import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.MachineAdapter;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.extensions.ExtensionInterface;
import de.dhbwloerrach.beaconlocation.extensions.SmartEyeGlassExtension;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.Machine;
import de.dhbwloerrach.beaconlocation.models.RssiAverageType;

/**
 * Created by Lukas on 31.07.2015.
 * Implements a fragment that shows a list of all machines
 */
public class MachinesFragment extends BaseFragment implements IBeaconListView {
    private MachineAdapter adapter;
    private Thread glassAdpaterThread;
    private ExtensionInterface extension;
    private ArrayList<Beacon> beaconsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machines, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!initialized) {
            adapter = new MachineAdapter(activity);
            initialized = true;
        }

        activity.getCommons().startMonitoring(this);

        adapter.clearItems();
        adapter.addItems(new DatabaseHandler(activity).getAllMachines());

        final ListView listView = (ListView) activity.findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to view #5
                Bundle bundle = new Bundle();
                bundle.putParcelable("machine", (Machine)listView.getItemAtPosition(position));
                // got to AddBeaconsToMachineFragement if selected Beacons notEmpty?
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.MACHINE, bundle);
            }
        });

        listView.setAdapter(adapter);
        listView.setEmptyView(activity.findViewById(R.id.emptyList_machines));

        extension = new SmartEyeGlassExtension();
        extension.connect(this.getActivity());
        final Activity context=this.getActivity();
        glassAdpaterThread=new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted())
                {
                    if(beaconsList!=null)
                    {
                        final BeaconList filteredBeacons = new BeaconList(beaconsList).filterByLast(5);
                        Machine closest=adapter.getClosestMachine(context,filteredBeacons);
                        @SuppressWarnings("all") // For ignoring allways true/false warings for debug flag. Replace "all" with specified warning if you find needed keyword
                        String maschineName=closest== null ? "No machine close to you.":("Close to \"" + closest.getName()+"\"" + (BeaconSettings.DEBUG?" ("+ adapter.debugRSSIValues +")":""));
                        extension.sendMessage(maschineName);
                    }
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

    @Override
    protected void createActionBarMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_machines, menu);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        if (itemId==R.id.add_machine) {
            buildDialog();
            return true;
        }
        else {return false;}
    }

    /**
     * Build a dialog to choose between create machine manually and use beacon scan
     */
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_titleMachine);

        builder.setPositiveButton(R.string.addMachineManual, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.ADD_MACHINE_MANUAL);

            }
        });

        builder.setNegativeButton(R.string.addMachineWithScan, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.getCommons().switchFragment(ActivityCommons.FragmentType.BEACON_SEARCH);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void disconnectView() {
        activity.getCommons().stopMonitoring(this);
        glassAdpaterThread.interrupt();
        extension.disconnect();
    }

    @Override
    public void refreshList(ArrayList<Beacon> beacons) {
        beaconsList=beacons;
        final BeaconList filteredBeacons = new BeaconList(beaconsList).filterByLast(5);

        DatabaseHandler databaseHandler = new DatabaseHandler(activity);
        try {
            ArrayList<Machine.MachineInfoContainer> machineDistanceInfo= new ArrayList<>();
            //ArrayList<Integer> machinesInRange = new ArrayList<>();

            for(Machine machine : databaseHandler.getAllMachines()) {
                ArrayList<Beacon> machineBeacons = databaseHandler.getAllBeaconsByMachine(machine.getId());

                //boolean machineInRange = !machineBeacons.isEmpty();
                if(machineBeacons.isEmpty()){
                    // machine has no beacons
                    continue;
                }
                String beaconsValueList="";
                double machinesRSSIValue=0;
                for (Beacon beacon : machineBeacons) {
                    Beacon currentRealBeacon = filteredBeacons.getBeacon(beacon.getMinor());
                    if(!"".equals(beaconsValueList))
                    {
                        beaconsValueList+=", ";
                    }
                    if(currentRealBeacon == null) {
                        // Beacon is not visible
                        machinesRSSIValue+=1000;
                        beaconsValueList+= beacon.getUuid()+ " n/a";
                    }
                    else
                    {
                        double rssi = currentRealBeacon.getRssiByAverageType(RssiAverageType.None, 2);
                        machinesRSSIValue+=rssi;
                        beaconsValueList+= beacon.getUuid()+ " " + rssi;
                    }
                }
                machinesRSSIValue= machinesRSSIValue/machineBeacons.size();
                machineDistanceInfo.add(new Machine.MachineInfoContainer(machine.getId(), Beacon.getRssiDistanceStatus(machinesRSSIValue), ""));
            }

            adapter.setMachineDistanceInfo(machineDistanceInfo);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } finally {
            databaseHandler.close();
        }
    }
}
