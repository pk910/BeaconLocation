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


    public Integer getId() {
        return id;
    }

    public Machine setId(Integer id) {
        this.id = id;
        return this;

    }

    public String getName() {
        return name;
    }

    public Machine setName(String name) {
        this.name = name;
        return this;

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
        if (databaseHandler.getMachine(machine.getName())==null){
            return false;
        }
        else{
            return true;
        }
    }
}
