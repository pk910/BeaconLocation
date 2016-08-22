package de.dhbwloerrach.beaconlocation.extensions;

import android.content.Context;

/**
 * Extension Interface class
 */
public interface ExtensionInterface {

    public void connect(Context context);
    public void disconnect();
    public void sendMessage(String message);

}
