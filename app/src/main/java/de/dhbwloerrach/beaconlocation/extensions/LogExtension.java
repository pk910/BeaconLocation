package de.dhbwloerrach.beaconlocation.extensions;


import android.content.Context;
import android.util.Log;

public class LogExtension implements ExtensionInterface {

    private String TAG = "LogExtension";

    @Override
    public void connect(Context context) {
        // Nothing to do for now.
    }

    @Override
    public void disconnect() {
        // Nothing to do for now.
    }

    @Override
    public void sendMessage(String message) {
        Log.d(TAG, message);
    }
}
