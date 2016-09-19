package de.dhbwloerrach.beaconlocation;

/**
 * Created by enio on 8/3/16.
 * Holder for global BeaconSettings
 */
public final class BeaconSettings {
    public static final int RSSI_AVERAGE_SECONDS = 2;
    public static final int BEACONLIST_REFRESH_INTERVAL_SECONDS = 5;
    public static final boolean DEBUG=true;

    private BeaconSettings() {} // Prevent instantiation by declaring constructor private.
}
