package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.meteoblue.*;


/**
 * * Fragment zur Anzeige von meteoblue Wetterdaten
 */

public class WeatherFragment extends BaseFragment implements WeatherListener {
    private LocationResolver locationResolver;
    View currentView;
    WeatherRequest weather;
    public static final int PERMISSIONS_REQUEST_LOCATION_RESOLVER = 1;

    public static final String LOG_TAG = "Wetter Fragment";

    public void setContext(Activity context) {
        locationResolver = new LocationResolver(context);
        WeatherData.setAppContext(context);

        weather = new WeatherRequest();
        weather.addWeatherListener(this);
        locationResolver.addLocationListener(weather);
    }


    /**
     * Create Fragment
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationResolver = new LocationResolver(activity);
        WeatherData.setAppContext(activity);

        weather = new WeatherRequest();
        weather.addWeatherListener(this);
        locationResolver.addLocationListener(weather);
    }

    @Override
    public void onResume() {
        super.onResume();
        weather.requestWeather(false);
        locationResolver.startLocationListener();
        Location loc = locationResolver.getLastKnownLocation();
        if(loc != null) {
            weather.onLocationChanged(loc);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        locationResolver.stopLocationListener();
    }

    /**
     * Android 5 permission management
     */
     /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION_RESOLVER: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    locationResolver.startLocationListener();
                } else {
                    // permission denied
                }
                return;
            }
            // other requests?
        }
    }*/

    public LocationResolver getLocationResolver() {
        return locationResolver;
    }

    /**
     * Create view for Fragment
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance state
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment
        currentView = inflater.inflate(R.layout.fragment_weather, container, false);

        return currentView;
    }

    @Override
    public void onWeatherReceived(WeatherData weather) {
        TextView weatherView = (TextView) currentView.findViewById(R.id.weatherData);

        StringBuilder strb = new StringBuilder(120);
        strb.append(weather.getDate())
        .append("\nTemp: ")
        .append(weather.getTempAvg())
        .append(" (Min: ")
        .append(weather.getTempMin())
        .append(", Max: ")
        .append(weather.getTempMax())
        .append(")\nWind: ")
        .append(weather.getWindAvg())
        .append(' ')
        .append(weather.getWindDir())
        .append(" (Min: ")
        .append(weather.getWindMin())
        .append(", Max: ")
        .append(weather.getWindMax())
        .append(")\nPrecipitation Probability: ")
        .append(weather.getRainPossibility())
        .append("%\nRel. Humidity: ")
        .append(weather.getHumidityAvg())
        .append(" (Min: ")
        .append(weather.getHumidityMin())
        .append(", Max: ")
        .append(weather.getHumidityMax())
        .append(")\n");

        ImageView weatherImageView = (ImageView) currentView.findViewById(R.id.weatherImage);
        weatherImageView.setImageResource(weather.getCodeDayPic());

        weatherView.setText(strb.toString());


        weather = weather.getNextDay();
        if(weather == null) {
            return;
        }

        weatherView = (TextView) currentView.findViewById(R.id.weatherData2);

        strb = new StringBuilder();
        strb.append(weather.getDate())
        .append("\nTemp: ")
        .append(weather.getTempAvg())
        .append(" (Min: ")
        .append(weather.getTempMin())
        .append(", Max: ")
        .append(weather.getTempMax())
        .append(")\nWind: ")
        .append(weather.getWindAvg())
        .append(' ')
        .append(weather.getWindDir())
        .append(" (Min: ")
        .append(weather.getWindMin())
        .append(", Max: ")
        .append(weather.getWindMax())
        .append(")\nPrecipitation Probability: ")
        .append(weather.getRainPossibility())
        .append("%\nRel. Humidity: ")
        .append(weather.getHumidityAvg())
        .append(" (Min: ")
        .append(weather.getHumidityMin())
        .append(", Max: ")
        .append(weather.getHumidityMax())
        .append(")\n");

        weatherImageView = (ImageView) currentView.findViewById(R.id.weatherImage2);
        weatherImageView.setImageResource(weather.getCodeDayPic());

        weatherView.setText(strb.toString());

        weather = weather.getNextDay();
        if(weather == null) {
            return;
        }

        weatherView = (TextView) currentView.findViewById(R.id.weatherData3);

        strb = new StringBuilder();
        strb.append(weather.getDate())
        .append("\nTemp: ")
        .append(weather.getTempAvg())
        .append(" (Min: ")
        .append(weather.getTempMin())
        .append(", Max: ")
        .append(weather.getTempMax())
        .append(")\nWind: ")
        .append(weather.getWindAvg())
        .append(' ')
        .append(weather.getWindDir())
        .append(" (Min: ")
        .append(weather.getWindMin())
        .append(", Max: ")
        .append(weather.getWindMax())
        .append(")\nPrecipitation Probability: ")
        .append(weather.getRainPossibility())
        .append("%\nRel. Humidity: ")
        .append(weather.getHumidityAvg())
        .append(" (Min: ")
        .append(weather.getHumidityMin())
        .append(", Max: ")
        .append(weather.getHumidityMax())
        .append(")\n");


        weatherImageView = (ImageView) currentView.findViewById(R.id.weatherImage3);
        weatherImageView.setImageResource(weather.getCodeDayPic());

        weatherView.setText(strb.toString());
    }

    @Override
    protected void createActionBarMenu(Menu menu) {
    // dont want one!
    }

    @Override
    protected boolean handleMenuClick(int itemId) {
        return false;
        // dont want one!
    }

    @Override
    protected void disconnectView() {
        // nothing to add here yet.
    }
}
