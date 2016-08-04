package de.dhbwloerrach.beaconlocation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Lukas on 23.07.2015.
 * Implements a list of Beacons
 */
public class BeaconList extends ArrayList<Beacon> {

    @SuppressWarnings("unused")
    static final long serialVersionUID = 3456456L;

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

    private void sortByRSSI() {
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
    }

    private void sortByMinor() {
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
    }

    public void sort(FilterTyp filterTyp){
        if(filterTyp== FilterTyp.Minor){
            sortByMinor();
        }
        else if(filterTyp== FilterTyp.RSSI){
            sortByRSSI();
        }
    }

    public Beacon getBeacon(int minor) {
        for (Beacon beacon : this) {
            if (beacon.getMinor() == minor) {
                return beacon;
            }
        }
        return null;
    }
}