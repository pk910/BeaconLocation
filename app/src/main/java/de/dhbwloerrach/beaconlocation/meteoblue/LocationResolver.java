package de.dhbwloerrach.beaconlocation.meteoblue;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by pk910 on 18.08.2016.
 *
 */
public class LocationResolver implements LocationListener {
    private LocationManager locationManager;
    private ArrayList<LocationListener> locationListeners = new ArrayList<>();
    private Location currentBestLocation;
    private boolean isRunning = false;

    public LocationResolver(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        startLocationListener();
    }

    public void addLocationListener(LocationListener listener) {
        locationListeners.add(listener);
        if(currentBestLocation != null) {
            listener.onLocationChanged(currentBestLocation);
        }
    }

    /* Unused
    public void delLocationListener(LocationListener listener) {
        locationListeners.remove(listener);
    }
    */

    public void startLocationListener() {
        if(!isRunning) {
            isRunning = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 20, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 50, this);
        }
    }

    public void stopLocationListener() {
        if(isRunning) {
            try {
                locationManager.removeUpdates(this);
                isRunning = false;
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.e("LocationResolver", e.toString());
            }
        }
    }

    public Location getLastKnownLocation() {
        Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocationGPS == null) {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            return lastKnownLocationGPS;
        }
    }

    /** Determines whether one location reading is better than the current location fix
     * @param location  The new location that you want to evaluate
     * @param currentBestLocation  The current location fix, to which you want to compare the new one.
     */
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 60*2*1000;
        boolean isSignificantlyOlder = timeDelta < -60*2*1000;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location,
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse.
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentBestLocation == null || isBetterLocation(location, currentBestLocation)) {
            currentBestLocation = location;
            for(LocationListener listener : locationListeners) {
                listener.onLocationChanged(location);
            }
        }
        Log.e("Test","GPS");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        for(LocationListener listener : locationListeners) {
            listener.onStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        for(LocationListener listener : locationListeners) {
            listener.onProviderEnabled(provider);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        for(LocationListener listener : locationListeners) {
            listener.onProviderDisabled(provider);
        }
    }
}
