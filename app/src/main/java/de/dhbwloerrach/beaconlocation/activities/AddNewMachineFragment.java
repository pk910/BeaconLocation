package de.dhbwloerrach.beaconlocation.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.models.Beacon;

/**
 * Created by alirei on 31.07.2015.
 * Implement a fragment that allows a user to add a machine by selecting a beacon from a list
 */
public class AddNewMachineFragment extends AddMachineBaseFragment {
    private ArrayList<Beacon> selectedBeacons = new ArrayList<>();
    private ActivityCommons commons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_machines, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commons = activity.getCommons();
        selectedBeacons = getArguments().getParcelableArrayList("selectedBeacons");

        BeaconAdapter adapter = new BeaconAdapter(activity);
        ListView beaconList = (ListView) activity.findViewById(R.id.beaconList);
        for (Beacon beacon : selectedBeacons){
            adapter.addItem(beacon);
        }
        beaconList.setAdapter(adapter);
        beaconList.setEmptyView(activity.findViewById(R.id.emptyList_add_machines));


        final Button cancelButton = (Button) activity.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commons.switchFragment(ActivityCommons.FragmentType.MACHINES_VIEW);

            }
        });
        final Button CommitButton = (Button) activity.findViewById(R.id.button_create);
        CommitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Textfeld auslesen
                final EditText textField = (EditText) activity.findViewById(R.id.editText);
                ArrayList<Beacon> beacons = selectedBeacons;
                addMachine(textField, beacons, ActivityCommons.FragmentType.MACHINES_VIEW);
            }


        });
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        //
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return true;
    }

    @Override
    protected void disconnectView() {
        //
    }
}
