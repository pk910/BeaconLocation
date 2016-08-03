package de.dhbwloerrach.beaconlocation.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lukas on 23.07.2015.
 * Implements a list of distance values for beacons
 */
public class DistanceList extends ArrayList<TimedDistance> {
    public DistanceList getLast(int seconds){
        DistanceList result = new DistanceList();
        long margin = new Date().getTime() - TimeUnit.SECONDS.toMillis(seconds);
        for(TimedDistance distance : this) {
            if(distance.getTimestamp().getTime() > margin) {
                result.add(distance);
            }
        }
        return result;
    }

    public double getAverageDistance() {
        double sum = 0;
        for (TimedDistance distance : this)
            sum += distance.getDistance();

        return sum / this.size();
    }
}
