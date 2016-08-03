package de.dhbwloerrach.beaconlocation.models;

import java.util.Date;

/**
 * Created by Lukas on 23.07.2015.
 * Implements a class that takes a timestamp and a distance for a beacon
 */
class TimedDistance {
    private double distance;
    private Date timestamp;

    public TimedDistance(double distance){
        this.distance = distance;
        this.timestamp = new Date();
    }

    public double getDistance() {
        return distance;
    }

    @SuppressWarnings("unused")
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getTimestamp() {
        return (Date) timestamp.clone();
    }

    @SuppressWarnings("unused")
    public void setTimestamp(Date timestamp) {
        this.timestamp =(Date) timestamp.clone();
    }
}
