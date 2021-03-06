package de.dhbwloerrach.beaconlocation.activities;

import android.app.Fragment;
import android.view.Menu;

/**
 * Created by David on 7/31/15.
 * Extens the standard fragment with some required elements
 */
public abstract class BaseFragment extends Fragment {
    MainActivity activity;

    boolean initialized = false;

    /**
     * Set a new custom fragment menu
     * @param menu Menu
     */
    protected abstract void createActionBarMenu(Menu menu);

    /**
     * Handle the menu item click for the current fragment
     * @param itemId int
     * @return boolean
     */
    protected abstract boolean handleMenuClick(int itemId);

    /**
     * Disconnect the current fragment from the activity
     */
    protected abstract void disconnectView();

    /**
     * Set the current activity
     * @param activity MainActivity
     */
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
