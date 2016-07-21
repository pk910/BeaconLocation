package de.dhbwloerrach.beaconlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 31.07.2015.
 */
public class MachineAdapter extends ArrayAdapter<Machine> {
    private final Context context;
    private ArrayList<Machine> machines = new ArrayList<>();
    private DecimalFormat distanceFormat = new DecimalFormat("#m");
    private ArrayList<Integer> machineIdInRange = new ArrayList<>();

    public MachineAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
    }

    /**
     * Add one item
     * @param item Machine
     */
    public void addItem(Machine item) {
        machines.add(item);
    }

    /**
     * Add multiple items
     * @param items Collection
     */
    public void addItems(Collection<Machine> items) {
        machines.addAll(items);
    }

    /**
     * Clear the adapter
     */
    public void clearItems() {
        machines.clear();
    }

    /**
     * Set machine which are currently in range
     * @param machineIdInRange ArrayList
     */
    public void setMachineIdInRange(ArrayList<Integer> machineIdInRange) {
        this.machineIdInRange.clear();
        this.machineIdInRange.addAll(machineIdInRange);
    }

    @Override
    public int getCount() {
        return machines.size();
    }

    @Override
    public Machine getItem(int position) {
        return machines.get(position);
    }

    @Override
    public int getPosition(Machine beacon) {
        return machines.indexOf(beacon);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Machine machine = machines.get(position);

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listitem_machine, parent, false);

        // 3. Get the two text view from the rowView
        TextView valueViewMinor = (TextView) rowView.findViewById(R.id.machineName);
        ImageView inRangeIcon = (ImageView) rowView.findViewById(R.id.machineInRange);
        ImageView warningIcon = (ImageView) rowView.findViewById(R.id.machineWarning);

        // 4. Set the text for textView
        valueViewMinor.setText(machine.getName().toString());

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        try {
            int beaconsCount = databaseHandler.getAllBeaconsByMachine(machine.getId()).size();
            warningIcon.setVisibility(beaconsCount == 0 ? View.VISIBLE : View.GONE);
            inRangeIcon.setVisibility(beaconsCount != 0 ? View.VISIBLE : View.GONE);
        } finally {
            databaseHandler.close();
        }

        inRangeIcon.setImageResource(machineIdInRange.contains(machine.getId()) ? R.mipmap.circle_green : R.mipmap.circle_grey);

        // 5. retrn rowView
        return rowView;
    }
}
