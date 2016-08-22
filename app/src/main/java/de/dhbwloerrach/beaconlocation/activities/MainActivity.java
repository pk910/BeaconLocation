package de.dhbwloerrach.beaconlocation.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sonyericsson.extras.liveware.aef.registration.Registration;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.extensions.smarteyeglass.ExtensionService;


public class MainActivity extends Activity {
    private ActivityCommons commons;

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


        // TODO: Evtl. Auslagern (Singleton)
        /*
         * Make sure ExtensionService of your SmartEyeglass app has already
         * started.
         * This is normally started automatically when user enters your app
         * on SmartEyeglass, although you can initialize it early using
         * request intent.
         */
        if (ExtensionService.Object == null) {
            Intent intent = new Intent(Registration.Intents
                    .EXTENSION_REGISTER_REQUEST_INTENT);
            Context context = getApplicationContext();
            intent.setClass(context, ExtensionService.class);
            context.startService(intent);
        }



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

        startExtension();

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


    /**
     *  Start the app with the message "Hello SmartEyeglass"
     */
    public void startExtension() {
        Log.d("MachineLocator", "Debug - startExtension();");
        // Check ExtensionService is ready and referenced
        if (ExtensionService.Object != null) {
            ExtensionService.Object
                    .sendMessageToExtension("Hello MachineLocator!");
        }
    }
}
