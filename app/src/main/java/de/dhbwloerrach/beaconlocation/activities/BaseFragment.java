package de.dhbwloerrach.beaconlocation.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Created by David on 7/31/15.
 */
public abstract class BaseFragment extends Fragment {
    protected MainActivity activity;

    protected boolean initialized = false;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Set the current activity
     * @param activity MainActivity
     * @return BaseFragment
     */
    public BaseFragment setActivity(MainActivity activity) {
        this.activity = activity;
        return this;
    }
}
