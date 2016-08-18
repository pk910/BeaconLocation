package de.dhbwloerrach.beaconlocation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;

/**
 * Created by Lukas on 04.08.2015.
 * Implements a fragmant that allows an user to manually add a maschine (with entering the mino-ID)
 */
public class AddManualMachineFragment extends AddMachineBaseFragment {
    private ArrayList<EditText> dynamicMinorIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_machine_manual, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!initialized) {
            initialized = true;
        }

        dynamicMinorIds = new ArrayList<>();
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_newmachine, menu);
    }

    /**
     * Add a new empty input field for insert a manual beacon minor id
     */
    private void addBeaconInputField() {
        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.minors);

        EditText editText = new EditText(activity);
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        editText.setHint(R.string.minorId);
        editText.setId(9500 + dynamicMinorIds.size());

        editText.setSingleLine();
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        dynamicMinorIds.add(editText);
        layout.addView(editText);
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        switch (itemId) {
            case R.id.add_beacon:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addBeaconInputField();
                    }
                });
                return true;

            case R.id.save_machine:
                // save machine to database
                DatabaseHandler databaseHandler = new DatabaseHandler(activity);

                EditText editText = (EditText) activity.findViewById(R.id.editText2);

                ArrayList<Beacon> beacons = new ArrayList<>();
                for(EditText minor : dynamicMinorIds){
                    if (minor != null && minor.getText() != null && !minor.getText().toString().isEmpty()) {
                        int minorId;
                        try{
                            minorId = Integer.parseInt(minor.getText().toString());
                        } catch (NumberFormatException ex) {
                            continue;
                        }
                        Beacon beacon = databaseHandler.getBeacon(minorId);
                        if (beacon == null) {
                            beacon = new Beacon()
                                    .setMinor(minorId);
                        }
                        beacons.add(beacon);
                    }
                }
                addMachine(editText, beacons, ActivityCommons.FragmentType.MACHINES_VIEW);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void disconnectView() {
        //
    }
}
