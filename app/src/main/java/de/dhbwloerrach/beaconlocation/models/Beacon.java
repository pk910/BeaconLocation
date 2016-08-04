package de.dhbwloerrach.beaconlocation.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;

/**
 * Created by alirei on 20.07.2015.
 * Implements a class that represents a single beacon
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Beacon implements Parcelable{
    private String uuid = "";
    private double distance;
    private String bluetoothName;
    private int txpower;
    private int rssi;
    private final RssiList rssis = new RssiList();
    private String bluetoothAddress;
    private final DistanceList distances = new DistanceList();
    private Date lastSeen;
    private Integer major = 0;
    private Integer minor = 0;
    private Integer id = 0;
    private Integer machineId = 0;
    private Boolean front_left;
    private Boolean front_right;
    private Boolean back_left;
    private Boolean back_right;

    /**
     * Default constructor. Do not use beacon created with this constructor until uuid, major and minor are change manually.
     */
    public Beacon(){
        uuid="generic beacon... ";
        major=-1;
        minor=-1;
    }

    private Beacon(Parcel in){
        uuid = in.readString();
        major = in.readInt();
        minor = in.readInt();
    }

    public String getUuid() {
        return uuid;
    }

    public Beacon setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getMajor() {
        return major;
    }

    public Beacon setMajor(Integer major) {
        this.major = major;
        return this;
    }

    public Integer getMinor() {
        return minor;
    }

    public Beacon setMinor(Integer minor) {
        this.minor = minor;
        return this;
    }

    private double getDistance() {
        return distance;
    }

    public Beacon setDistance(double distance) {
        this.distance = distance;
        this.distances.add(new TimedDistance(distance));
        return this;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public Beacon setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
        return this;
    }

    @SuppressWarnings("unused")
    public Integer getTxpower() {
        return txpower;
    }

    public Beacon setTxpower(Integer txpower) {
        this.txpower = txpower;
        return this;
    }

    public Integer getRssi() {
        return rssi;
    }

    public Beacon setRssi(Integer rssi) {
        this.rssi = rssi;
        this.rssis.add(new TimedRssi(rssi));
        return this;
    }

    @SuppressWarnings("unused")
    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public Beacon setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        return this;
    }

    public boolean hasDistanceIn(int seconds) {
        return !this.distances.getLast(seconds).isEmpty();
    }

    @SuppressWarnings("unused")
    public boolean hasRssiIn(int seconds) {
        return !this.rssis.getLast(seconds).isEmpty();
    }

    @SuppressWarnings("unused")
    public double getAverageDistance(int seconds) {
        DistanceList last = this.distances.getLast(seconds);
        if(last.size() > 0) {
            return last.getAverageDistance();
        }
        else {
            return this.getDistance();
        }
    }

    private RssiList getRssis(int seconds) {
        return this.rssis.getLast(seconds);
    }

    @SuppressWarnings("unused")
    public Date getLastSeen() {
        return (Date) lastSeen.clone();
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen =(Date) lastSeen.clone();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Boolean getFront_left() {
        return front_left;
    }

    public void setFront_left(Boolean bool) {
        this.front_left = bool;
    }

    public Boolean getFront_right() {
        return front_right;
    }

    public void setFront_right(Boolean bool) {
        this.front_right = bool;
    }

    public Boolean getBack_left() {
        return back_left;
    }

    public void setBack_left(Boolean bool) {
        this.back_left = bool;
    }

    public Boolean getBack_right() {
        return back_right;
    }

    public void setBack_right(Boolean bool) {
        this.back_right = bool;
    }

    @Override
    public String toString() {
        return this.getUuid() + " " + this.getMajor() + " " + this.getMinor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}
        if (o == null || getClass() != o.getClass()){ return false;}

        Beacon beacon = (Beacon) o;

//        if (!uuid.equals(beacon.uuid)) return false;
//        if (!major.equals(beacon.major)) return false;
        return minor.equals(beacon.minor);

    }

    @Override
    public int hashCode() {
        //int result = uuid.hashCode();
        //result = 31 * result + major.hashCode();
        //result = 31 * result + minor.hashCode();
        return minor.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeInt(major);
        dest.writeInt(minor);
    }

    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    public boolean checkBecaoninDB (Beacon beacon, DatabaseHandler databaseHandler){
        Beacon databaseBeacon = databaseHandler.getBeacon(beacon.getMinor()); //, beacon.getMajor(), beacon.getUuid()
        return databaseBeacon != null;
    }

    public double getRssiByAverageType(RssiAverageType averageType, int seconds) {
        switch (averageType) {
            case None:
                return getRssi() * 1d;

            case Average:
                return getRssis(seconds).getAverage();

            case SmoothedAverage:
                return getRssis(seconds).getSmoothAverage();

            default:
                return 0d;
        }
    }

    public RssiDistanceStatus getRssiDistanceStatus(double rssi) {
        if (rssi >= -70 && rssi < 0) {
            return RssiDistanceStatus.IN_RANGE;
        } else if (rssi < -70 && rssi >= -80) {
            return RssiDistanceStatus.NEAR_BY_RANGE;
        } else if (rssi < -80 && rssi >= -85) {
            return RssiDistanceStatus.AWAY;
        } else if (rssi < -85 && rssi >= -99) {
            return RssiDistanceStatus.FAR_AWAY;
        } else {
            return RssiDistanceStatus.UNKNOWN;
        }
    }

    public enum RssiDistanceStatus
    {
        UNKNOWN,
        IN_RANGE,
        NEAR_BY_RANGE,
        AWAY,
        FAR_AWAY,
    }
}
