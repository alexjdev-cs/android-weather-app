package com.example.prove06;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Alex Johnson
 * @since 20191023
 *
 * The WeatherRequester class is a Runnable which is used to fetch weather data for the user
 * on a background thread through an API.
 * <p>
 * Depending on what the user wants, it will call
 * one of two methods to initiate an API call through a UrlConnection, and expects JSON data
 * as the payload.
 */
public class WeatherRequester implements Runnable {

    // Member variables
    private WeakReference<Activity> callingActivity;
    private String city;
    private String requestType;


    // Constants
    private final String API_KEY;
    private final String CURRENT_TEMP;
    private final String FORECAST;
    private final String URL_CURRENT_WEATHER;
    private final String URL_FORECAST;

    // Getters
    public WeakReference<Activity> getCallingActivity() {
        return callingActivity;
    }

    public String getCity() {
        return city;
    }

    public String getRequestType() {
        return requestType;
    }



    /**
     * This is the only explicit constructor for WeatherRequester. It sets the members to
     * the values passed in.
     *
     * @param callingActivity Holds a WeakReference to the calling activity
     * @param city A string that contains the name of the city. Will be put in the
     *             query string for the API call.
     * @param requestType A string containing the type of request to be fulfilled; in this
     *                    program, the only two possible cases are "current_temp" and "forecast".
     * */
    WeatherRequester(WeakReference<Activity> callingActivity, String city, String requestType) {

        //Initialize member variables
        this.callingActivity = callingActivity;
        this.city = city;
        this.requestType = requestType;

        // Initialize member constants
        API_KEY = this.callingActivity.get().getString(R.string.api_key);
        CURRENT_TEMP = this.callingActivity.get().getString(R.string.arg_current_temp);
        FORECAST = this.callingActivity.get().getString(R.string.arg_forecast);
        URL_CURRENT_WEATHER = this.callingActivity.get().getString(R.string.url_current_weather);
        URL_FORECAST = this.callingActivity.get().getString(R.string.url_forecast);
    }

    /**
     * This is called when WeatherRequester is used as a Runnable on a thread.
     */
    @Override
    public void run() {

        // User wants current temperature
        if (getRequestType().equals(CURRENT_TEMP)) {
            String currentTemp = getCurrentTemp();

            if (currentTemp.equals(callingActivity.get().getString(R.string.cond_city_not_found))) {
                displayCityNotFound();
                return;
            }

            display(currentTemp);
        }

        // User wants a forecast
        else if (getRequestType().equals(FORECAST)) {
            WeatherForecast weatherForecast = getForecast();

            if (weatherForecast.code().equals("404")) {
                displayCityNotFound();
                return;
            }

            display(weatherForecast);
        }

    }

    private String getCurrentTemp() {
        URL url = buildURL(URL_CURRENT_WEATHER);
        Log.i("url_after_build", url.toString()); // DEBUG

        CurrentWeatherConditions currentWeatherConditions = requestCurrentApiData(url);

        if (currentWeatherConditions != null && currentWeatherConditions.code().equals("200")) {
            return currentWeatherConditions.getCondition("temp", true);
        }
        else if (currentWeatherConditions != null && (currentWeatherConditions.code().equals("404"))) {
            return callingActivity.get().getString(R.string.cond_city_not_found);
        }
        else {

        }
        return null;
    }

    private WeatherForecast getForecast() {
        URL url = buildURL(URL_FORECAST);
        Log.i("url_after_build", url.toString()); // DEBUG

        WeatherForecast weatherForecast = requestForecastApiData(url);

        return weatherForecast;
    }



    private URL buildURL(String urlPreQuery) {
        String url;

        String queryStr = getCallingActivity().get().getString(R.string.query_str);
        String charset = StandardCharsets.UTF_8.name();

        try {
            String encodedCity = URLEncoder.encode(getCity(), charset);
            String encodedApiKey = URLEncoder.encode(API_KEY, charset);

            url = urlPreQuery + String.format(queryStr, encodedCity, encodedApiKey);

            return new URL(url);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return null;
    }

    private CurrentWeatherConditions requestCurrentApiData(URL url) {

        String response = "";

        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
            InputStream inputStream = urlConnection.getInputStream();

            try (Scanner scanner = new Scanner(inputStream)) {
                response = scanner.useDelimiter("\\A").next();
                Log.d("api_response", response); // DEBUG
            }catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            return new CurrentWeatherConditions(404);
        }

        Gson gson = new Gson();
        CurrentWeatherConditions currentWeatherConditions = gson.fromJson(response, CurrentWeatherConditions.class);

        currentWeatherConditions.DEBUG_display_all_entities(); //DEBUG

        return currentWeatherConditions;
    }

    private WeatherForecast requestForecastApiData(URL url) {

        String response = "";

        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
            InputStream inputStream = urlConnection.getInputStream();

            try (Scanner scanner = new Scanner(inputStream)) {
                response = scanner.useDelimiter("\\A").next();
                Log.d("api_response", response); // DEBUG
            }catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            return new WeatherForecast(404);
        }

        Gson gson = new Gson();
        WeatherForecast weatherForecast = gson.fromJson(response, WeatherForecast.class);

        return weatherForecast;
    }

    private void display(String currentTemp) {
        final String toastMessage;
        String formatString = getCallingActivity().get().getString(R.string.toast_current_temp);

        toastMessage = String.format(formatString, getCity(), currentTemp);

        // Have an anonymous Runnable display the Toast on the UI thread
        getCallingActivity().get().runOnUiThread(new Runnable() {
            @Override
            public void run () {
                Toast toast = Toast.makeText(getCallingActivity().get(), toastMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void display(final WeatherForecast weatherForecast) {
        getCallingActivity().get().runOnUiThread(new Runnable() {
            @Override
            public void run () {
                List<WeatherForecastItem> weatherForecastItems = weatherForecast.getWeatherForecastItems();
                ListView listView = getCallingActivity().get().findViewById(R.id.lst_forecast);
                List<String> stringForecastItems = new ArrayList<>();

                for (WeatherForecastItem item : weatherForecastItems) {
                    stringForecastItems.add(item.toString());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getCallingActivity().get(), android.R.layout.simple_list_item_1, android.R.id.text1, stringForecastItems);
                listView.setAdapter(arrayAdapter);
            }
        });
    }

    private void displayCityNotFound() {
        final String toastMessage;

        toastMessage = callingActivity.get().getString(R.string.toast_city_not_found);

        // Have an anonymous Runnable display the Toast on the UI thread
        getCallingActivity().get().runOnUiThread(new Runnable() {
            @Override
            public void run () {
                Toast toast = Toast.makeText(getCallingActivity().get(), toastMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }







}
