package de.dhbwloerrach.beaconlocation.bluetooth;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.models.Beacon;

/**
 * Created by Lukas on 22.07.2015.
 * Interface implements a function for refreshing a beacon list
 */
public interface IBeaconListView {
    void refreshList(ArrayList<Beacon> beacons);
}
