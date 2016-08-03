package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.dhbwloerrach.beaconlocation.R;


public class MainActivity extends Activity {
    private ActivityCommons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
        //getActionBar().setElevation(0);
        setContentView(R.layout.activity_main);

        checkBluetoothState();

        commons = new ActivityCommons(this);
        commons.createDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commons.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        commons.setMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return commons.menuHandler(item);
    }

    /**
     * Get the ActivityCommons
     * @return ActivityCommons
     */
    public ActivityCommons getCommons() {
        return commons;
    }

    /**
     * Check the bluetooth state and ask the use to enable bluetooth
     */
    public void checkBluetoothState() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }
        if (mBluetoothAdapter.isEnabled()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.bluetoothDisabledCaption)
                .setMessage(R.string.bluetoothDisabledMessage)
                .setPositiveButton(R.string.activate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (!mBluetoothAdapter.isEnabled()) {
                            mBluetoothAdapter.enable();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        if(commons.getDrawer() == null || commons.fragmentStackCount()) {
            super.onBackPressed();
            return;
        }

        if (commons.getDrawer().isDrawerOpen()) {
            commons.getDrawer().closeDrawer();
            return;
        }

        commons.lastFragmentStackItem();
    }
}
