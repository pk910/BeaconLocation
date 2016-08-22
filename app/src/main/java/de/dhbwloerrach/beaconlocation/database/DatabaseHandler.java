package de.dhbwloerrach.beaconlocation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Salvo on 23.07.2015.
 * Implements a database managment class
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "beaconDB",
            TABLE_MACHINE = "machine",
            TABLE_BEACON = "beacon",
            KEY_MACHINE_ID = "id",
            KEY_BEACON_ID = "id",
            KEY_MACHINE_NAME = "name",
            KEY_BEACON_MINOR = "minor",
            KEY_BEACON_MAJOR = "major",
            KEY_BEACON_UUID = "UUID",
            KEY_BEACON_FRONT_LEFT = "FL",
            KEY_BEACON_FRONT_RIGHT = "FR",
            KEY_BEACON_BACK_LEFT = "BL",
            KEY_BEACON_BACK_RIGHT = "BR",
            KEY_BEACON_MACHINEID = "machineId";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MACHINE + "(" + KEY_MACHINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + KEY_MACHINE_NAME + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_BEACON + "(" + KEY_BEACON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + KEY_BEACON_MINOR + " INTEGER," + KEY_BEACON_MAJOR + " INTEGER," + KEY_BEACON_UUID + " VARCHAR," + KEY_BEACON_MACHINEID + " INTEGER," + KEY_BEACON_FRONT_LEFT + " BOOLEAN DEFAULT false," + KEY_BEACON_FRONT_RIGHT + " BOOLEAN DEFAULT false," + KEY_BEACON_BACK_LEFT + " BOOLEAN DEFAULT false," + KEY_BEACON_BACK_RIGHT + " BOOLEAN DEFAULT false)");

        //db.execSQL("INSERT INTO " + TABLE_MACHINE + "(" + KEY_MACHINE_ID + ", " + KEY_MACHINE_NAME + ") VALUES (1, 'TestMaschine')");
        //db.execSQL("INSERT INTO " + TABLE_MACHINE + "(" + KEY_MACHINE_ID + ", " + KEY_MACHINE_NAME + ") VALUES (2, 'Vorlesung')");

        //db.execSQL("INSERT INTO " + TABLE_BEACON +  "(" + KEY_BEACON_ID + ", " + KEY_BEACON_MINOR + ", "+ KEY_BEACON_MAJOR + ", " + KEY_BEACON_UUID + ", "+ KEY_BEACON_MACHINEID + ") VALUES (1, 510, 0, '01234567-89AB-CDEF-0123-000000000001', 1)");
        //db.execSQL("INSERT INTO " + TABLE_BEACON + "(" + KEY_BEACON_ID + ", " + KEY_BEACON_MINOR + ", " + KEY_BEACON_MAJOR + ", " + KEY_BEACON_UUID + ", "+ KEY_BEACON_MACHINEID + ") VALUES (2, 511, 0, '01234567-89AB-CDEF-0123-000000000001', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE);

        onCreate(db);
    }

    public void createBeacon(Beacon beacon) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_BEACON_MINOR, beacon.getMinor());
        values.put(KEY_BEACON_MAJOR, beacon.getMajor());
        values.put(KEY_BEACON_UUID, beacon.getUuid());
        values.put(KEY_BEACON_MACHINEID, beacon.getMachineId());
        values.put(KEY_BEACON_FRONT_LEFT, beacon.getFront_left());
        values.put(KEY_BEACON_FRONT_RIGHT, beacon.getFront_right());
        values.put(KEY_BEACON_BACK_LEFT, beacon.getBack_left());
        values.put(KEY_BEACON_BACK_RIGHT, beacon.getBack_right());

        db.insert(TABLE_BEACON, null, values);
        db.close();
    }

    public int createMachine(Machine machine) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MACHINE_NAME, machine.getName());

        db.insert(TABLE_MACHINE, null, values);
        db.close();

        SQLiteDatabase dbRead = getWritableDatabase();
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + TABLE_MACHINE, null);

        int id = 0;

        if (cursor.moveToLast()) {
            id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            dbRead.close();
        }
        return id;
    }

    public Machine getMachine(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MACHINE, new String[]{KEY_MACHINE_ID, KEY_MACHINE_NAME}, KEY_MACHINE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        Machine machine = null;

        if (cursor != null && cursor.moveToFirst()) {
            machine = new Machine(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
            cursor.close();
        }
        db.close();
        return machine;
    }

    public Beacon getBeacon(int minor) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_BEACON, new String[]{KEY_BEACON_ID, KEY_BEACON_MINOR, KEY_BEACON_MAJOR, KEY_BEACON_UUID, KEY_BEACON_MACHINEID, KEY_BEACON_FRONT_LEFT, KEY_BEACON_FRONT_RIGHT, KEY_BEACON_BACK_LEFT, KEY_BEACON_BACK_RIGHT}, KEY_BEACON_MINOR + "=?", new String[]{String.valueOf(minor)}, null, null, null, null);

        Beacon beacon = null;
        if (cursor != null && cursor.moveToFirst()) {
            beacon = new Beacon();
            beacon.setId(Integer.parseInt(cursor.getString(0)));
            beacon.setMinor(Integer.parseInt(cursor.getString(1)));
            beacon.setMajor(Integer.parseInt(cursor.getString(2)));
            beacon.setUuid(cursor.getString(3));
            beacon.setMachineId(Integer.parseInt(cursor.getString(4)));
            beacon.setFront_left(Boolean.parseBoolean(cursor.getString(5)));
            beacon.setFront_right(Boolean.parseBoolean(cursor.getString(6)));
            beacon.setBack_left(Boolean.parseBoolean(cursor.getString(7)));
            beacon.setBack_right(Boolean.parseBoolean(cursor.getString(8)));
            cursor.close();
        }
        db.close();
        return beacon;
    }

    public void deleteMachine(Machine machine) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MACHINE, KEY_MACHINE_ID + "=?", new String[]{String.valueOf(machine.getId())});
        db.close();
    }

    public void deleteBeacon(Beacon beacon) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BEACON, KEY_BEACON_ID + "=?", new String[]{String.valueOf(beacon.getId())});
        db.close();
    }

    @SuppressWarnings("unused")
    public int updateMachine(Machine machine) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MACHINE_NAME, machine.getName());

        int rowsAffected = db.update(TABLE_MACHINE, values, KEY_MACHINE_ID + "=?", new String[] { String.valueOf(machine.getId()) });
        db.close();

        return rowsAffected;
    }

    public void updateBeacon(Beacon beacon) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_BEACON_MINOR, beacon.getMinor());
        values.put(KEY_BEACON_MAJOR, beacon.getMajor());
        values.put(KEY_BEACON_UUID, beacon.getUuid());
        values.put(KEY_BEACON_MACHINEID, beacon.getMachineId());
        values.put(KEY_BEACON_FRONT_LEFT, beacon.getFront_left());
        values.put(KEY_BEACON_FRONT_RIGHT, beacon.getFront_right());
        values.put(KEY_BEACON_BACK_LEFT, beacon.getBack_left());
        values.put(KEY_BEACON_BACK_RIGHT, beacon.getBack_right());

        db.update(TABLE_BEACON, values, KEY_BEACON_ID + "=?", new String[] { String.valueOf(beacon.getId()) });
        db.close();
    }

    public List<Machine> getAllMachines() {
        List<Machine> machines = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MACHINE, null);

        if (cursor.moveToFirst()) {
            do {
                Machine machine = new Machine(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
                machines.add(machine);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return machines;
    }

    @SuppressWarnings("unused")
    public List<Beacon> getAllBeacons() {
        List<Beacon> beacons = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BEACON, null);

        if (cursor.moveToFirst()) {
            do {
                Beacon beacon = new Beacon();
                beacon.setId(Integer.parseInt(cursor.getString(0)));
                beacon.setMinor(Integer.parseInt(cursor.getString(1)));
                beacon.setMajor(Integer.parseInt(cursor.getString(2)));
                beacon.setUuid(cursor.getString(3));
                beacon.setMachineId(Integer.parseInt(cursor.getString(4)));
                beacon.setFront_left(Boolean.parseBoolean(cursor.getString(5)));
                beacon.setFront_right(Boolean.parseBoolean(cursor.getString(6)));
                beacon.setBack_left(Boolean.parseBoolean(cursor.getString(7)));
                beacon.setBack_right(Boolean.parseBoolean(cursor.getString(8)));
                beacons.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return beacons;
    }

    @SuppressWarnings("unused")
    private Beacon getBeacon(int minor, int major, String uuid) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_BEACON, new String[]{KEY_BEACON_ID, KEY_BEACON_MINOR, KEY_BEACON_MAJOR, KEY_BEACON_UUID, KEY_BEACON_MACHINEID, KEY_BEACON_FRONT_LEFT, KEY_BEACON_FRONT_RIGHT, KEY_BEACON_BACK_LEFT, KEY_BEACON_BACK_RIGHT}, KEY_BEACON_MINOR + "=? AND " + KEY_BEACON_MAJOR + "=? AND " + KEY_BEACON_UUID + "=?", new String[]{String.valueOf(minor), String.valueOf(major), uuid}, null, null, null, null);

        Beacon beacon;

        if (cursor == null) {
            db.close();
            return null;
        }else {
            if(cursor.moveToFirst()) {
                beacon = new Beacon();
                beacon.setId(Integer.parseInt(cursor.getString(0)));
                beacon.setMinor(Integer.parseInt(cursor.getString(1)));
                beacon.setMajor(Integer.parseInt(cursor.getString(2)));
                beacon.setUuid(cursor.getString(3));
                beacon.setMachineId(Integer.parseInt(cursor.getString(4)));
                beacon.setFront_left(Boolean.parseBoolean(cursor.getString(5)));
                beacon.setFront_right(Boolean.parseBoolean(cursor.getString(6)));
                beacon.setBack_left(Boolean.parseBoolean(cursor.getString(7)));
                beacon.setBack_right(Boolean.parseBoolean(cursor.getString(8)));
                cursor.close();
                db.close();
                return beacon;
            }else {
                return null;
            }
        }
    }
    public ArrayList<Beacon> getAllBeaconsByMachine(int machineId) {
        ArrayList<Beacon> beacons = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BEACON, new String[]{KEY_BEACON_ID, KEY_BEACON_MINOR, KEY_BEACON_MAJOR, KEY_BEACON_UUID, KEY_BEACON_MACHINEID, KEY_BEACON_FRONT_LEFT, KEY_BEACON_FRONT_RIGHT, KEY_BEACON_BACK_LEFT, KEY_BEACON_BACK_RIGHT}, KEY_BEACON_MACHINEID + "=?", new String[]{String.valueOf(machineId)}, null, null, null, null);


        if (cursor.moveToFirst()) {
            do {
                Beacon beacon = new Beacon();
                beacon.setId(Integer.parseInt(cursor.getString(0)));
                beacon.setMinor(Integer.parseInt(cursor.getString(1)));
                beacon.setMajor(Integer.parseInt(cursor.getString(2)));
                beacon.setUuid(cursor.getString(3));
                beacon.setMachineId(Integer.parseInt(cursor.getString(4)));
                beacon.setFront_left(Boolean.parseBoolean(cursor.getString(5)));
                beacon.setFront_right(Boolean.parseBoolean(cursor.getString(6)));
                beacon.setBack_left(Boolean.parseBoolean(cursor.getString(7)));
                beacon.setBack_right(Boolean.parseBoolean(cursor.getString(8)));
                beacons.add(beacon);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return beacons;
    }

    public Machine getMachine(String name) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MACHINE, new String[]{KEY_MACHINE_ID, KEY_MACHINE_NAME}, KEY_MACHINE_NAME + "=?", new String[]{String.valueOf(name)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Machine machine = new Machine(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
            cursor.close();
            db.close();
            return machine;
        }
        db.close();
        return null;
    }

}
