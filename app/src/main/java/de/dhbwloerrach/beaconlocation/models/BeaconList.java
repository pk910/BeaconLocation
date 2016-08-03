package de.dhbwloerrach.beaconlocation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Lukas on 23.07.2015.
 */
public class BeaconList extends ArrayList<Beacon> {

    public BeaconList() {
        super();
    }

    public BeaconList(ArrayList<Beacon> beacons) {
        super();
        addAll(beacons);
    }

    public BeaconList filterByLast(int seconds) {
        BeaconList result = new BeaconList();
        for (Beacon beacon : this) {
            if (beacon.hasDistanceIn(seconds)) {
                result.add(beacon);
            }
        }
        return result;
    }

    protected BeaconList SortByRSSI() {
        Collections.sort(this, new Comparator<Beacon>() {
            @Override
            public int compare(Beacon lhs, Beacon rhs) {
                double tempDiff = lhs.getRssi() - rhs.getRssi();
                if (Double.compare(tempDiff, 0.0d) == 0) {
                    return 0;
                }

                if (Double.compare(tempDiff, 0.0d) == -1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return this;
    }

    protected BeaconList SortByMinor() {
        Collections.sort(this, new Comparator<Beacon>() {
            @Override
            public int compare(Beacon lhs, Beacon rhs) {
                double tempDiff = lhs.getMinor() - rhs.getMinor();
                if (Double.compare(tempDiff, 0.0d) == 0) {
                    return 0;
                }
                if (Double.compare(tempDiff, 0.0d) == -1) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return this;
    }

    public void Sort(FilterTyp filterTyp){
        switch (filterTyp){
            case Minor:
                SortByMinor();
                break;
            case RSSI:
                SortByRSSI();
                break;
        }
    }

    public Beacon getBeacon(int minor) {
        for (Beacon beacon : this) {
            if (beacon.getMinor() != minor) {
                continue;
            }

            return beacon;
        }

        return null;
    }
}