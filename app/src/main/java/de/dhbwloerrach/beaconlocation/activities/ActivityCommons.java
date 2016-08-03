package de.dhbwloerrach.beaconlocation.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.bluetooth.BeaconTools;
import de.dhbwloerrach.beaconlocation.bluetooth.IBeaconListView;

/**
 * Created by Lukas on 31.07.2015.
 * Manages fragmants and some common elements
 */
public class ActivityCommons implements Drawer.OnDrawerItemClickListener {
    private Menu menu;
    private final MainActivity context;
    private BeaconTools beaconTools;
    private Drawer drawer;
    private BaseFragment fragment;

    private AddBeaconsToMachineFragment addBeaconsToMachineFragment;
    private MachineFragment machineFragment;

    private final List<Map.Entry<FragmentType, Bundle>> fragmentStack = new ArrayList<>();

    /**
     * @param context MainActivity
     */
    public ActivityCommons(MainActivity context) {
        this.context = context;
    }

    /**
     * Set a new custom fragment menu
     * @param menu Menu
     * @return ActivityCommons
     */
    public ActivityCommons setMenu(Menu menu) {
        this.menu = menu;

        if(fragment != null) {
            menu.clear();
            fragment.createActionBarMenu(menu);
        }

        return this;
    }

    /**
     * Handle the menu item click for the current fragment
     * @param item MenuItem
     * @return boolean
     */
    public boolean menuHandler(MenuItem item) {
        return fragment.handleMenuClick(item.getItemId());
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        switchFragment((FragmentType) iDrawerItem.getTag());
        drawer.closeDrawer();

        return true;
    }

    /**
     * Switch the current Fragment without passing some arguments
     * @param type FragmentType
     */
    public void switchFragment(FragmentType type) {
        switchFragment(type, null);
    }

    /**
     * Switch the current Fragment and set some arguments
     * @param type FragmentType
     * @param bundle Bundle
     */
    public void switchFragment(FragmentType type, Bundle bundle) {
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        boolean allowSwitchBack = true;
        BaseFragment lastFragment = fragment;

        if(lastFragment != null) {
            lastFragment.disconnectView();
            fragmentTransaction.remove(lastFragment);
            fragmentTransaction.commit();
            fragmentTransaction = fragmentManager.beginTransaction();
        }

        switch (type) {
            case BEACON_SEARCH:
                fragment = new BeaconsFragment();
                break;

            case MACHINES_VIEW:
                fragment = new MachinesFragment();
                break;

            case ADD_MACHINE:
                fragment = new AddNewMachineFragment();
                allowSwitchBack = false;
                break;
            case ADD_MACHINE_MANUAL:
                fragment = new AddManualMachineFragment();
                allowSwitchBack = false;
                break;
            case ADD_BEACON_TO_MACHINE:
                if(addBeaconsToMachineFragment==null){
                    addBeaconsToMachineFragment = new AddBeaconsToMachineFragment();
                }

                fragment = addBeaconsToMachineFragment;
                allowSwitchBack = false;
                break;
            case MACHINE:
                if(machineFragment==null){
                    machineFragment = new MachineFragment();
                }

                fragment = machineFragment;
                break;
        }

        if (allowSwitchBack) {
            fragmentStack.add(new AbstractMap.SimpleEntry<>(type, bundle));
        }

        fragment.setActivity(context);
        if(bundle != null) {
            fragment.setArguments(bundle);
        } else {
            fragment.setArguments(new Bundle());
        }

        if(menu != null) {
            menu.clear();
            fragment.createActionBarMenu(menu);
        }

        fragmentTransaction.replace(R.id.mainView, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Setup and create the drawer (left menu)
     */
    public void createDrawer(){
        drawer = new DrawerBuilder()
                .withActivity(context)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new SecondaryDrawerItem().withTag(FragmentType.MACHINES_VIEW).withName(R.string.menu_machineView),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withTag(FragmentType.BEACON_SEARCH).withName(R.string.menu_beaconView)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        switchFragment(FragmentType.MACHINES_VIEW);
    }

    /**
     * Returns the drawer
     * @return Drawer
     */
    public Drawer getDrawer() {
        return drawer;
    }

    /**
     * Start the beacon monitoring
     * @param view IBeaconListView
     */
    public void startMonitoring(IBeaconListView view) {
        if (beaconTools == null) {
            beaconTools = new BeaconTools(context, view);
        } else {
            beaconTools.addView(view);
        }
    }

    /**
     * Stop the beacon monitoring
     * @param view IBeaconListView
     */
    public void stopMonitoring(IBeaconListView view) {
        if(beaconTools == null) {
            return;
        }

        beaconTools.removeView(view);
    }

    /**
     * Unbind the beacon tools and his additional bindings
     */
    public void unbind() {
        beaconTools.unbind();
    }

    /**
     * Returns the fragmentStack
     * @return List
     */
    public List<Map.Entry<FragmentType, Bundle>> getFragmentStack() {
        return fragmentStack;
    }

    /**
     * Returns if the size of the fragmentStack is high enough for use
     * @return int
     */
    public boolean fragmentStackCount() {
        return fragmentStack.size() >= 2;
    }

    /**
     * Switch the fragment to the last one
     */
    public void lastFragmentStackItem() {
        if(!fragmentStackCount()) {
            return;
        }

        fragmentStack.remove(fragmentStack.size() - 1);
        Map.Entry<FragmentType, Bundle> entry = fragmentStack.remove(fragmentStack.size() - 1);
        switchFragment(entry.getKey(), entry.getValue());
    }

    /**
     * Defines the available FragmentTypes
     */
    public enum FragmentType
    {
        BEACON_SEARCH,
        MACHINES_VIEW,
        ADD_MACHINE,
        ADD_MACHINE_MANUAL,
        ADD_BEACON_TO_MACHINE,
        MACHINE,
    }
}
