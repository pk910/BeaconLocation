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

import de.dhbwloerrach.beaconlocation.BeaconSettings;
import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.database.DatabaseHandler;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
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
    public String debugRSSIValues;

    public MachineAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
        debugRSSIValues ="";
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
     * Updates the machine status of each machine
     * @param machineDistanceInfo ArrayList
     */
    public void setMachineDistanceInfo(ArrayList<Machine.MachineInfoContainer> machineDistanceInfo) {
        for(Machine.MachineInfoContainer container: machineDistanceInfo)
        {
            for(int i=0;i<machines.size();i++)
            {
                if(container.machineID== machines.get(i).getId())
                {
                    machines.get(i).incertMachineInfos(container);
                }
            }
        }
    }

    public Machine getClosestMachine(Context context, BeaconList beacons){
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
                    distanceValues[i]=0;
                    continue;
                }
                distanceValues[i]=0;
                for(int j=0;j<machineBeacons.size();j++)
                {
                    distanceValues[i]+= translateRssiDistanceStatus(Beacon.getRssiDistanceStatus(beacons.getBeacon(machineBeacons.get(j).getMinor()).getRssiByAverageType(RssiAverageType.None, 2)));
                }
                distanceValues[i]=distanceValues[i]/machineBeacons.size();
            }
            int pos=getMaxIndex(distanceValues);
            if(distanceValues[pos]<3)
            {
                return null;
            }
            else
            {
                if(BeaconSettings.DEBUG)
                {
                    ArrayList<Beacon> machineBeacons = databaseHandler.getAllBeaconsByMachine(machines.get(pos).getId());
                    debugRSSIValues ="";
                    for(int i=0;i<machineBeacons.size();i++)
                    {
                        if(i!=0) {
                            debugRSSIValues += ",";
                        }
                        Beacon a=beacons.getBeacon(machineBeacons.get(i).getMinor());
                        debugRSSIValues +=a.getMinor()+" "+a.getRssi();
                    }
                }
                return machines.get(pos);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private int getMaxIndex(double[] array){
        int index=0;
        for(int i=1;i<array.length;i++){
            if(array[i] > array[index]){
                index=i;
            }
        }
        return index;
    }

    private int translateRssiDistanceStatus(Beacon.RssiDistanceStatus status){
        if(status== Beacon.RssiDistanceStatus.IN_RANGE)
        {
            return 4;
        }
        else if(status== Beacon.RssiDistanceStatus.NEAR_BY_RANGE)
        {
            return 3;
        }
        else if(status== Beacon.RssiDistanceStatus.AWAY)
        {
            return 2;
        }
        else if(status== Beacon.RssiDistanceStatus.FAR_AWAY)
        {
            return 1;
        }
        else if(status== Beacon.RssiDistanceStatus.UNKNOWN)
        {
            return 0;
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
        private TextView valueBeaconInfo;
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
            mViewHolder.valueBeaconInfo = (TextView) view.findViewById(R.id.machineBeacons);
            mViewHolder.inRangeIcon = (ImageView) view.findViewById(R.id.machineInRange);
            mViewHolder.warningIcon = (ImageView) view.findViewById(R.id.machineWarning);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        // 4. Set the text for textView
        mViewHolder.valueViewMinor.setText(machine.getName());
        mViewHolder.valueBeaconInfo.setText(machine.getBeaconsValueList());


        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        try {
            int beaconsCount = databaseHandler.getAllBeaconsByMachine(machine.getId()).size();
            mViewHolder.warningIcon.setVisibility(beaconsCount == 0 ? View.VISIBLE : View.GONE);
            mViewHolder.inRangeIcon.setVisibility(beaconsCount == 0 ? View.GONE: View.VISIBLE);
        } finally {
            databaseHandler.close();
        }
        mViewHolder.inRangeIcon.setImageResource(getAccordingImage(machine.getStatus()));

        // 5. return rowView
        return view;
    }

    private int getAccordingImage(Beacon.RssiDistanceStatus status){
        if(status== Beacon.RssiDistanceStatus.IN_RANGE)
        {
            return R.mipmap.circle_green;
        }
        else if(status == Beacon.RssiDistanceStatus.NEAR_BY_RANGE)
        {
            return R.mipmap.circle_yellow;
        }
        else if(status == Beacon.RssiDistanceStatus.AWAY)
        {
            return R.mipmap.circle_orange;
        }
        else if(status == Beacon.RssiDistanceStatus.FAR_AWAY)
        {
            return R.mipmap.circle_grey;
        }
        else{
            return R.mipmap.circle_grey;
        }
    }
}
