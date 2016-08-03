package de.dhbwloerrach.beaconlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by Lukas on 31.07.2015.
 * Implements an adapter class for machines
 */
public class MachineAdapter extends ArrayAdapter<Machine> {
    private final Context context;
    private ArrayList<Machine> machines = new ArrayList<>();
    //private DecimalFormat distanceFormat = new DecimalFormat("#m");
    private ArrayList<Integer> machineIdInRange = new ArrayList<>();

    public MachineAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
    }

    /*
    /**
     * Add one item
     * @param item Machine
     * /
    public void addItem(Machine item) {
        machines.add(item);
    }
    */

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

    static class ViewHolder {

        private TextView valueViewMinor;
        private ImageView inRangeIcon;
        private ImageView warningIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Machine machine = machines.get(position);


        ViewHolder mViewHolder;

        if (null == convertView) {

            mViewHolder = new ViewHolder();

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.listitem_machine, parent, false);

            // 3. Get the two text view from the rowView
            TextView valueViewMinor = (TextView) rowView.findViewById(R.id.machineName);
            ImageView inRangeIcon = (ImageView) rowView.findViewById(R.id.machineInRange);
            ImageView warningIcon = (ImageView) rowView.findViewById(R.id.machineWarning);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // 4. Set the text for textView
        mViewHolder.valueViewMinor.setText(machine.getName());

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        try {
            int beaconsCount = databaseHandler.getAllBeaconsByMachine(machine.getId()).size();
            mViewHolder.warningIcon.setVisibility(beaconsCount == 0 ? View.VISIBLE : View.GONE);
            mViewHolder.inRangeIcon.setVisibility(beaconsCount != 0 ? View.VISIBLE : View.GONE);
        } finally {
            databaseHandler.close();
        }

        mViewHolder.inRangeIcon.setImageResource(machineIdInRange.contains(machine.getId()) ? R.mipmap.circle_green : R.mipmap.circle_grey);

        // 5. return rowView
        return convertView;
    }
}
