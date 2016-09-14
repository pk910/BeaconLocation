package de.dhbwloerrach.beaconlocation.activities;

import android.app.Activity;
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

        locationResolver.startLocationListener();
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
        weather.requestWeather(false);

        return currentView;
    }

    @Override
    public void OnWeatherReceived(WeatherData weather) {
        TextView weatherView = (TextView) currentView.findViewById(R.id.weatherData);

        StringBuilder strb = new StringBuilder();
        strb.append(weather.getDate());
        strb.append("\n");
        /*strb.append(weather.getCodeDesc());  // Does not work.
        strb.append("\n");*/
        strb.append("Temp: ");
        strb.append(weather.getTempAvg());
        strb.append(" (Min: ");
        strb.append(weather.getTempMin());
        strb.append(", Max: ");
        strb.append(weather.getTempMax());
        strb.append(")\n");

        ImageView weatherImageView = (ImageView) currentView.findViewById(R.id.weatherImage);
        weatherImageView.setImageResource(weather.getCodeDayPic());

        weatherView.setText(strb.toString());


        weather = weather.getNextDay();
        if(weather == null)
            return;

        weatherView = (TextView) currentView.findViewById(R.id.weatherData2);

        strb = new StringBuilder();
        strb.append(weather.getDate());
        strb.append("\n");
        /*strb.append(weather.getCodeDesc()); // Does not work.
        strb.append("\n");*/
        strb.append("Temp: ");
        strb.append(weather.getTempAvg());
        strb.append(" (Min: ");
        strb.append(weather.getTempMin());
        strb.append(", Max: ");
        strb.append(weather.getTempMax());
        strb.append(")\n");
        strb.append("Wind: ");
        strb.append(weather.getWindAvg());
        strb.append(" ");
        strb.append(weather.getWindDir());
        strb.append(" (Min: ");
        strb.append(weather.getWindMin());
        strb.append(", Max: ");
        strb.append(weather.getWindMax());
        strb.append(")\n");
        strb.append("Precipitation Probability: ");
        strb.append(weather.getRainPossibility());
        strb.append("%\n");
        strb.append("Rel. Humidity: ");
        strb.append(weather.getHumidityAvg());
        strb.append(" (Min: ");
        strb.append(weather.getHumidityMin());
        strb.append(", Max: ");
        strb.append(weather.getHumidityMax());
        strb.append(")\n");

        weatherImageView = (ImageView) currentView.findViewById(R.id.weatherImage2);
        weatherImageView.setImageResource(weather.getCodeDayPic());

        weatherView.setText(strb.toString());

        weather = weather.getNextDay();
        if(weather == null)
            return;

        weatherView = (TextView) currentView.findViewById(R.id.weatherData3);

        strb = new StringBuilder();
        strb.append(weather.getDate());
        strb.append("\n");
        /*strb.append(weather.getCodeDesc()); // Does not work.
        strb.append("\n");*/
        strb.append("Temp: ");
        strb.append(weather.getTempAvg());
        strb.append(" (Min: ");
        strb.append(weather.getTempMin());
        strb.append(", Max: ");
        strb.append(weather.getTempMax());
        strb.append(")\n");
        strb.append("Wind: ");
        strb.append(weather.getWindAvg());
        strb.append(" ");
        strb.append(weather.getWindDir());
        strb.append(" (Min: ");
        strb.append(weather.getWindMin());
        strb.append(", Max: ");
        strb.append(weather.getWindMax());
        strb.append(")\n");
        strb.append("Precipitation Probability: ");
        strb.append(weather.getRainPossibility());
        strb.append("%\n");
        strb.append("Rel. Humidity: ");
        strb.append(weather.getHumidityAvg());
        strb.append(" (Min: ");
        strb.append(weather.getHumidityMin());
        strb.append(", Max: ");
        strb.append(weather.getHumidityMax());
        strb.append(")\n");


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
