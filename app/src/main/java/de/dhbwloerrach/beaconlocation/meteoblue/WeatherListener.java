package de.dhbwloerrach.beaconlocation.meteoblue;

/**
 * Created by pk910 on 18.08.2016.
 *
 */
public interface WeatherListener {
    void onWeatherReceived(WeatherData weather);
    void OnWeatherLocatorStatusChanged(boolean isLocationServiceEnabled);
}
