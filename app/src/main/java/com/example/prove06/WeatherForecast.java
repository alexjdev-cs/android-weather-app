package com.example.prove06;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class WeatherForecast {
    private City city;
    @SerializedName("cod")
    private String code;

    @SerializedName("list")
    private List<WeatherForecastItem> weatherForecastItems;

    public List<WeatherForecastItem> getWeatherForecastItems() {
        return weatherForecastItems;
    }

    public WeatherForecast (int code) {
        switch (code) {
            case 404:
                this.code = "404";
        }
    }

    public String code() {
        return code;
    }

    private void DEBUG_display_all_entities () {

    }
}
