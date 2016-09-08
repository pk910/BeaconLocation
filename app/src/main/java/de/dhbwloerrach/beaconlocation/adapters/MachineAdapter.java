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
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;
import de.dhbwloerrach.beaconlocation.models.RssiAverageType;

/**
 * Created by Lukas on 31.07.2015.
 * Implements an adapter class for machines
 */
public class MachineAdapter extends ArrayAdapter<Machine> {
    private final Context context;
    private final ArrayList<Machine> machines = new ArrayList<>();
    //private DecimalFormat distanceFormat = new DecimalFormat("#m");
    private final ArrayList<Integer> machineIdInRange = new ArrayList<>();

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

    public Machine getClosestMachine(Context context){
        if(machines.size()==0)
        {
            return null;
        }
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler(context);
            double distanceValues[]=new double[machines.size()];
            for(int i=0;i<machines.size();i++) {
                ArrayList<Beacon> machineBeacons = databaseHandler.getAllBeaconsByMachine(machines.get(i).getId());
                if(machineBeacons.size()==0)
                {
                    distanceValues[i]=10;
                    continue;
                }
                distanceValues[i]=0;
                for(int j=0;j<machineBeacons.size();j++)
                {
                    distanceValues[i]+= translateRssiDistanceStatus(machineBeacons.get(j).getRssiDistanceStatus(machineBeacons.get(j).getRssiByAverageType(RssiAverageType.None, 2)));
                }
                distanceValues[i]=distanceValues[i]/machineBeacons.size();
            }
            return machines.get(getMinValue(distanceValues));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private int getMinValue(double[] array){
        int index=0;
        for(int i=1;i<array.length;i++){
            if(array[i] < array[index]){
                index=i;
            }
        }
        return index;
    }

    private int translateRssiDistanceStatus(Beacon.RssiDistanceStatus status){
        if(status== Beacon.RssiDistanceStatus.IN_RANGE)
        {
            return 0;
        }
        else if(status== Beacon.RssiDistanceStatus.NEAR_BY_RANGE)
        {
            return 1;
        }
        else if(status== Beacon.RssiDistanceStatus.AWAY)
        {
            return 2;
        }
        else if(status== Beacon.RssiDistanceStatus.FAR_AWAY)
        {
            return 3;
        }
        else if(status== Beacon.RssiDistanceStatus.UNKNOWN)
        {
            return 5;
        }
        return -1;
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

        View view = convertView;
        ViewHolder mViewHolder;

        if (null == view) {

            mViewHolder = new ViewHolder();

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            view = inflater.inflate(R.layout.listitem_machine, parent, false);

            // 3. Get the two text view from the rowView
            mViewHolder.valueViewMinor = (TextView) view.findViewById(R.id.machineName);
            mViewHolder.inRangeIcon = (ImageView) view.findViewById(R.id.machineInRange);
            mViewHolder.warningIcon = (ImageView) view.findViewById(R.id.machineWarning);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        // 4. Set the text for textView
        mViewHolder.valueViewMinor.setText(machine.getName());

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        try {
            int beaconsCount = databaseHandler.getAllBeaconsByMachine(machine.getId()).size();
            mViewHolder.warningIcon.setVisibility(beaconsCount == 0 ? View.VISIBLE : View.GONE);
            mViewHolder.inRangeIcon.setVisibility(beaconsCount == 0 ? View.GONE: View.VISIBLE);
        } finally {
            databaseHandler.close();
        }

        mViewHolder.inRangeIcon.setImageResource(machineIdInRange.contains(machine.getId()) ? R.mipmap.circle_green : R.mipmap.circle_grey);

        // 5. return rowView
        return view;
    }
}
