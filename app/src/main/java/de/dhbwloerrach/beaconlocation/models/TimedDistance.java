package de.dhbwloerrach.beaconlocation.models;

import java.util.Date;

/**
 * Created by Lukas on 23.07.2015.
 */
public class TimedDistance {
    private double distance;
    private Date timestamp;

    public TimedDistance(double distance){
        this.distance = distance;
        this.timestamp = new Date();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
