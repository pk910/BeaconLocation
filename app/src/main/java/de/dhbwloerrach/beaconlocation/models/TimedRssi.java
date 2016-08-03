package de.dhbwloerrach.beaconlocation.models;

import java.util.Date;

/**
 * Created by Lukas on 31.07.2015.
 * Implements a class that holds a rssi value and a timestamp from a beacon
 */
public class TimedRssi {
    private int rssi;
    private Date timestamp;

    public TimedRssi(int rssi){
        this.rssi = rssi;
        this.timestamp = new Date();
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public Date getTimestamp() {
        return (Date) timestamp.clone();
    }

    public TimedRssi setTimestamp(Date timestamp) {
        this.timestamp =(Date) timestamp.clone();
        return this;
    }
}
