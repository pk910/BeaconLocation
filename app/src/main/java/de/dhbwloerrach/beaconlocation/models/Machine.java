package de.dhbwloerrach.beaconlocation.models;

import android.os.Parcel;
import android.os.Parcelable;

import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;

/**
 * Created by Salvo on 23.07.2015.
 * Implements a class that represents a single machine
 */
public class Machine implements Parcelable {


    private String name;
    private Integer id;
    private Beacon.RssiDistanceStatus machineRssiDistanceStatus= Beacon.RssiDistanceStatus.UNKNOWN;
    private String beaconsValueList="";

    public Machine(int id, String name){
        this.id=id;
        this.name=name;
    }

    // Constructor for parcelable
    private Machine(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    public void incertMachineInfos(MachineInfoContainer container)
    {
        if(this.id== container.machineID)
        {
            this.machineRssiDistanceStatus=container.machineRssiDistanceStatus;
            this.beaconsValueList = container.beaconsValueList;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Beacon.RssiDistanceStatus getStatus()
    {
        return machineRssiDistanceStatus;
    }

    public String getBeaconsValueList()
    {
        return this.beaconsValueList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public boolean checkMachineinDB (Machine machine,DatabaseHandler databaseHandler){
        return databaseHandler.getMachine(machine.getName()) != null;
    }

    public static class MachineInfoContainer{
        public int machineID;
        public Beacon.RssiDistanceStatus machineRssiDistanceStatus;
        public String beaconsValueList;

        public MachineInfoContainer(int machineID, Beacon.RssiDistanceStatus machineRssiDistanceStatus, String beaconsValueList)
        {
            this.machineID=machineID;
            this.machineRssiDistanceStatus= machineRssiDistanceStatus;
            this.beaconsValueList=beaconsValueList;
        }

    }
}
