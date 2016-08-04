package de.dhbwloerrach.beaconlocation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lukas on 31.07.2015.
 * Implements a list of timedRssi values
 */
public class RssiList extends ArrayList<TimedRssi> {

    @SuppressWarnings("unused")
    static final long serialVersionUID = 3456458L;

    public RssiList getLast(int seconds){
        RssiList result = new RssiList();
        long margin = new Date().getTime() - TimeUnit.SECONDS.toMillis(seconds);
        for(TimedRssi rssi : this) {
            if(rssi.getTimestamp().getTime() > margin) {
                result.add(rssi);
            }
        }
        return result;
    }

    public Double getAverage() {
        if(this.isEmpty())
            return 0.0;

        double sum = 0;
        for (TimedRssi distance : this)
            sum += distance.getRssi();

        return sum / this.size();
    }

    public Double getSmoothAverage() {
        if(this.isEmpty())
            return 0.0;
        if(this.size() < 3)
            return this.getAverage();

        ArrayList<Integer> ordered = new ArrayList<>();
        for (TimedRssi rssi : this){
            ordered.add(rssi.getRssi());
        }
        Collections.sort(ordered);
        double tenth = ordered.size() * 0.1;
        for (int i = 0; i <= tenth; i++){
            ordered.remove(0);
        }
        for (int i = 0; i <= tenth; i++){
            ordered.remove(ordered.size() - 1);
        }
        double sum = 0;
        for (int rssi : ordered)
            sum += rssi;

        return sum / ordered.size();
    }
}