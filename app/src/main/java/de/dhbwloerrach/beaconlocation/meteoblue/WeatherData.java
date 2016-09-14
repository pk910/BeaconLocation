package de.dhbwloerrach.beaconlocation.meteoblue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;

/**
 * Created by pk910 on 18.08.2016.
 *
 */

public class WeatherData {
    private static Context appContext;

    public static void setAppContext(Context context) {
        appContext = context;
    }

    private WeatherData nextDay = null;

    private String dataDate;
    private int dataCode;
    private String dataCodeDesc;
    private int dataCodeDayPicId, dataCodeNightPicId;
    private int dataUVIndex;
    private double dataTempMax, dataTempMin, dataTempAvg;
    private double dataFeltTempMax, dataFeltTempMin;
    private double dataRainValue, dataSnowValue;
    private double dataWindMin, dataWindMax, dataWindAvg, dataHumidityMin, dataHumidityMax, dataHumidityAvg;
    private String dataWindDir;
    private int dataRainPossibility, dataAccuracy;

    public WeatherData(JSONObject json) {
        parseJsonData(json);
    }

    private WeatherData() {
        // internal constructor
    }

    // Copy constructor for reducing lint warnings :)
    public WeatherData(WeatherData weather)
    {
        this.nextDay=weather.getNextDay();
        this.dataDate=weather.getDate();
        this.dataCode=weather.getCode();
        this.dataCodeDesc=weather.getCodeDesc();
        this.dataCodeDayPicId=weather.getCodeDayPic();
        this.dataCodeNightPicId=weather.getCodeNightPic();
        this.dataUVIndex=weather.getUVIndex();
        this.dataTempMax=weather.getTempMax();
        this.dataTempMin=weather.getTempMin();
        this.dataTempAvg=weather.getTempAvg();
        this.dataFeltTempMax=weather.getFeltTempMax();
        this.dataFeltTempMin=weather.getFeltTempMin();
        this.dataRainValue=weather.getRainValue();
        this.dataSnowValue=weather.getSnowValue();
        this.dataWindMin=weather.getWindMin();
        this.dataWindMax=weather.getWindMax();
        this.dataWindAvg=weather.getWindAvg();
        this.dataHumidityMin=weather.getHumidityMin();
        this.dataHumidityMax=weather.getHumidityMax();
        this.dataHumidityAvg=weather.getHumidityAvg();
        this.dataHumidityAvg=weather.getHumidityAvg();
        this.dataWindDir=weather.getWindDir();
        this.dataRainPossibility=weather.getRainPossibility();
        this.dataAccuracy=weather.getAccuracy();


    }

    public WeatherData getNextDay() {
        return nextDay;
    }

    private void parseJsonData(JSONObject json) {
        try {
            JSONObject daydata = json.getJSONObject("data_day");

            JSONArray timeData = daydata.getJSONArray("time");
            int dayCount = timeData.length();
            WeatherData weatherDays[] = new WeatherData[dayCount];
            weatherDays[0] = this;
            for(int i = 1; i < dayCount; i++) {
                weatherDays[i] = new WeatherData();
                weatherDays[i-1].nextDay = weatherDays[i];
            }

            for (Iterator<String> keys = daydata.keys(); keys.hasNext();){
                String key = keys.next();
                JSONArray values = daydata.getJSONArray(key);
                for(int i = 0; i < dayCount; i++) {
                    WeatherData data = weatherDays[i];

                    try {
                        if (key.equalsIgnoreCase("time"))
                            data.dataDate = values.getString(i);
                        else if (key.equalsIgnoreCase("pictocode")) {
                            int pictocode = values.getInt(i);
                            data.dataCode = pictocode;

                            String dataCodeDayPic = String.format(Locale.GERMANY,"meteoblue_%02d_day", pictocode);
                            data.dataCodeDayPicId = appContext.getResources().getIdentifier(dataCodeDayPic, "drawable", appContext.getPackageName());

                            String dataCodeNightPic = String.format(Locale.GERMANY,"meteoblue_%02d_night", pictocode);
                            data.dataCodeNightPicId = appContext.getResources().getIdentifier(dataCodeNightPic, "drawable", appContext.getPackageName());

                            String dataCodeDescRes = String.format(Locale.GERMANY,"meteoblue_code%02d", pictocode);
                            int dataCodeDescResId = appContext.getResources().getIdentifier(dataCodeDescRes, "string", appContext.getPackageName());
                            if(dataCodeDescResId != 0)
                                data.dataCodeDesc = appContext.getString(dataCodeDescResId);
                        } else if (key .equalsIgnoreCase("uvindex"))
                            data.dataUVIndex = values.getInt(i);
                        else if (key.equalsIgnoreCase("temperature_max"))
                            data.dataTempMax = values.getDouble(i);
                        else if (key.equalsIgnoreCase("temperature_min"))
                            data.dataTempMin = values.getDouble(i);
                        else if (key.equalsIgnoreCase("temperature_mean"))
                            data.dataTempAvg = values.getDouble(i);
                        else if (key.equalsIgnoreCase("felttemperature_max"))
                            data.dataFeltTempMax = values.getDouble(i);
                        else if (key.equalsIgnoreCase("felttemperature_min"))
                            data.dataFeltTempMin = values.getDouble(i);
                        else if (key.equalsIgnoreCase("winddirection"))
                            data.dataWindDir = getWindDir(values.getInt(i));
                        else if (key.equalsIgnoreCase("precipitation_probability"))
                            data.dataRainPossibility = values.getInt(i);
                        else if (key.equalsIgnoreCase("predictability"))
                            data.dataAccuracy = values.getInt(i);
                        else if (key.equalsIgnoreCase("precipitation"))
                            data.dataRainValue = values.getDouble(i);
                        else if (key.equalsIgnoreCase("snowfraction"))
                            data.dataSnowValue = values.getDouble(i);
                        else if (key.equalsIgnoreCase("windspeed_max"))
                            data.dataWindMax = values.getDouble(i);
                        else if (key.equalsIgnoreCase("windspeed_min"))
                            data.dataWindMin = values.getDouble(i);
                        else if (key.equalsIgnoreCase("windspeed_mean"))
                            data.dataWindAvg = values.getDouble(i);
                        else if (key.equalsIgnoreCase("relativehumidity_max"))
                            data.dataHumidityMax = values.getDouble(i);
                        else if (key.equalsIgnoreCase("relativehumidity_min"))
                            data.dataHumidityMin = values.getDouble(i);
                        else if (key.equalsIgnoreCase("relativehumidity_mean"))
                            data.dataHumidityAvg = values.getDouble(i);
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getWindDir(int dir){
        if (intBetween(dir, 23, 67)) return "Northeast";
        if (intBetween(dir, 68, 112)) return "East";
        if (intBetween(dir, 113, 157)) return "Southeast";
        if (intBetween(dir, 158, 202)) return "South";
        if (intBetween(dir, 203, 246)) return "Southwest";
        if (intBetween(dir, 247, 291)) return "West";
        if (intBetween(dir, 292, 336)) return "Northwest";
        return "North";
    }

    private boolean intBetween( int cmp, int a, int b){
        return cmp >= a && cmp <= b;
    }

    public String getDate() {
        return dataDate;
    }

    public int getCode() {
        return dataCode;
    }

    public String getCodeDesc() {
        return dataCodeDesc;
    }

    public int getCodeDayPic() {
        return dataCodeDayPicId;
    }

    public int getCodeNightPic() {
        return dataCodeNightPicId;
    }

    public int getUVIndex() {
        return dataUVIndex;
    }

    public double getTempMax() {
        return dataTempMax;
    }

    public double getTempAvg() {
        return dataTempAvg;
    }

    public double getTempMin() {
        return dataTempMin;
    }

    public double getFeltTempMax() {
        return dataFeltTempMax;
    }

    public double getFeltTempMin() {
        return dataFeltTempMin;
    }

    public double getRainValue() {
        return dataRainValue;
    }

    public double getSnowValue() {
        return dataSnowValue;
    }

    public double getWindMin() {
        return dataWindMin;
    }

    public double getWindMax() {
        return dataWindMax;
    }

    public double getWindAvg() {
        return dataWindAvg;
    }

    public double getHumidityMin() {
        return dataHumidityMin;
    }

    public double getHumidityMax() {
        return dataHumidityMax;
    }

    public double getHumidityAvg() {
        return dataHumidityAvg;
    }

    public int getRainPossibility() {
        return dataRainPossibility;
    }

    public String getWindDir() {
        return dataWindDir;
    }

    public int getAccuracy() {
        return dataAccuracy;
    }

}
