package de.dhbwloerrach.beaconlocation.activities;

import android.app.ActionBar;
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
import de.dhbwloerrach.beaconlocation.extensions.ExtensionInterface;
import de.dhbwloerrach.beaconlocation.extensions.LogExtension;
import de.dhbwloerrach.beaconlocation.extensions.SmartEyeGlassExtension;
import de.dhbwloerrach.beaconlocation.extensions.ToastExtension;


public class MainActivity extends Activity {
    private ActivityCommons commons;
    private ExtensionInterface extension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable color = new ColorDrawable(Color.parseColor("#d3d3d3"));
        ActionBar ab = getActionBar();
        if (null != ab) {
            ab.setBackgroundDrawable(color);
        }

        //getActionBar().setElevation(0);
        setContentView(R.layout.activity_main);

        checkBluetoothState();


        extension = new ToastExtension();
        //extension = new LogExtension();
        extension.connect(getApplicationContext());



        commons = new ActivityCommons(this);
        commons.createDrawer();
    }

    @Override
    protected void onDestroy() {
        extension.disconnect();
        commons.unbind();
        super.onDestroy();
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
    private void checkBluetoothState() {
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

        extension.sendMessage("Test");

        if (commons.getDrawer().isDrawerOpen()) {
            commons.getDrawer().closeDrawer();
            return;
        }

        if(commons.getDrawer() == null || commons.isFragmentStackCountTooSmall()) {
            super.onBackPressed();
            return;
        }

        commons.lastFragmentStackItem();
    }

}
