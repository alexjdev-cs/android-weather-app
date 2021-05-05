package com.example.prove06;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

class WeatherForecastItem {

    @SerializedName("dt_txt")
    private String date;

    @SerializedName("main")
    private Map<String, String> metrics;

    @SerializedName("weather")
    private List<Map <String, String>> weather;


    public String getDate() {
        return date;
    }

    public String toString() {
        String s;

        float fHighTemp = Float.parseFloat(metrics.get("temp_max"));
        int highTemp = Math.round(fHighTemp);

        float fLowTemp = Float.parseFloat(metrics.get("temp_min"));
        int lowTemp = Math.round(fLowTemp);

        s = "Date Time Group: " + getDate() + "\n";
        s += "High Temp: " + highTemp + "°F\n";
        s += "Low Temp: " + lowTemp + "°F\n";
        s += "Humidity: " + metrics.get("humidity")  + "%\n";
        s += "Weather Type: " + weather.get(0).get("main") + "\n";

        return s;
    }

}
