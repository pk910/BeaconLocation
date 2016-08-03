package de.dhbwloerrach.beaconlocation.bluetooth;

import java.util.ArrayList;

import de.dhbwloerrach.beaconlocation.models.Beacon;

/**
 * Created by Lukas on 22.07.2015.
 */
public interface IBeaconListView {
    void refreshList(ArrayList<Beacon> beacons);
}
