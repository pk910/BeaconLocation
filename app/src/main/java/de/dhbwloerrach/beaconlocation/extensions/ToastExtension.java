package de.dhbwloerrach.beaconlocation.extensions;

import android.content.Context;
import android.widget.Toast;

public class ToastExtension implements ExtensionInterface {

    private Context context;

    @Override
    public void connect(Context context) {
        this.context=context;
    }

    @Override
    public void disconnect() {
        // Nothing to do for now.
    }

    @Override
    public void sendMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
