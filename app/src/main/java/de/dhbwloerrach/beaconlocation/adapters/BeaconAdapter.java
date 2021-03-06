package de.dhbwloerrach.beaconlocation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Locale;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;
import de.dhbwloerrach.beaconlocation.models.RssiAverageType;

/**
 * Created by Lukas on 22.07.2015.
 * Implements a class that manages a list off beacons
 */
public class BeaconAdapter extends ArrayAdapter<Beacon> {
    private final Context context;
    private final BeaconList beacons = new BeaconList();
    //private DecimalFormat distanceFormat = new DecimalFormat("#m");
    private final DecimalFormat rssiFormat = new DecimalFormat("#");
    private FilterTyp filterTyp = FilterTyp.Minor;
    private RssiAverageType rssiAverageType = RssiAverageType.SmoothedAverage;

    public BeaconAdapter(Context context) {
        super(context, R.layout.listitem_beacon);
        this.context = context;
    }

    /**
     * Get the filter typ from the beacon list
     * @return FilterTyp
     */
    public FilterTyp getFilterTyp() {
        return filterTyp;
    }

    /**
     * Set the filter typ for the beacon list
     * @param filterTyp FilterTyp
     */
    public void setFilterTyp(FilterTyp filterTyp) {
        this.filterTyp = filterTyp;
    }

    /**
     * Return the current rssi calculation mode
     * @return RssiAverageType
     */
    public RssiAverageType getRssiAverageType() {
        return rssiAverageType;
    }

    /**
     * Set the current rssi value calculation mode
     * @param rssiAverageType RssiAverageType
     */
    public void setRssiAverageType(RssiAverageType rssiAverageType) {
        this.rssiAverageType = rssiAverageType;
    }

    /**
     * Add one item
     * @param item Beacon
     */
    public void addItem(Beacon item) {
        beacons.add(item);
        beacons.sort(filterTyp);
    }

    /**
     * Add multiple Items
     * @param items Collection
     */
    public void addItems(Collection<Beacon> items) {
        beacons.addAll(items);
        beacons.sort(filterTyp);
    }

    /**
     * Clear the adapter
     */
    public void clearItems() {
        beacons.clear();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Beacon getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public int getPosition(Beacon beacon) {
        return beacons.indexOf(beacon);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolder {

        private TextView valueViewMinor;
        private TextView valueViewRssi;
        private ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder mViewHolder;

        if (null == view) {
            mViewHolder = new ViewHolder();

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get convertView from inflater
            view = inflater.inflate(R.layout.listitem_beacon, parent, false);

            // 3. Get the two text view from the convertView
            mViewHolder.valueViewMinor = (TextView) view.findViewById(R.id.minor);
            mViewHolder.valueViewRssi = (TextView) view.findViewById(R.id.rssi);
            mViewHolder.image = (ImageView) view.findViewById(R.id.circle);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }


        // 4. Set the text for textView
        mViewHolder.valueViewMinor.setText(String.format(Locale.ENGLISH, "%d",beacons.get(position).getMinor()));

        double rssi = beacons.get(position).getRssiByAverageType(rssiAverageType, 2);
        switch (beacons.get(position).getRssiDistanceStatus(rssi)) {
            case IN_RANGE:
                mViewHolder.image.setImageResource(R.mipmap.circle_green);
                break;

            case NEAR_BY_RANGE:
                mViewHolder.image.setImageResource(R.mipmap.circle_yellow);
                break;

            case AWAY:
                mViewHolder.image.setImageResource(R.mipmap.circle_orange);
                break;

            case FAR_AWAY:
                mViewHolder.image.setImageResource(R.mipmap.circle_red);
                break;

            case UNKNOWN:
                mViewHolder.image.setImageResource(R.mipmap.circle_grey);
                break;
            default:
                Log.e("Error","Unkown beacon distance");
                break;
        }

        mViewHolder.valueViewRssi.setText((rssi == 0) ? "-" : rssiFormat.format(rssi));

        // 5. retrn convertView
        return view;
    }
}
