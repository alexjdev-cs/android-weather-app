package com.example.prove06;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class CurrentWeatherConditions {

    @SerializedName("name")
    private String city;

    @SerializedName("id")
    private String cityID;

    @SerializedName("cod")
    private String code;

    @SerializedName("main")
    private Map<String, String> conditions;

    public CurrentWeatherConditions() {
        city = "";
        cityID = "";
        code = "";
        conditions = new HashMap<>();
    }

    public CurrentWeatherConditions(int code) {

        switch (code) {
            case 404:
                city = "";
                cityID = "";
                this.code = "404";
                conditions = null;
        }
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) {
        this.city = city;
    }

    public String getCityID() {
        return cityID;
    }

    private void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    private void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public String getCondition (String condition) {
        if (conditions.containsKey(condition)) {
            return conditions.get(condition);
        }

        return null;
    }

    public String getCondition (String condition, boolean roundResult) {
        int resultInt;
        String resultString;

        if (conditions.containsKey(condition)) {
            String value = conditions.get(condition);

            if (roundResult = true) {
                try {
                    Float num = Float.parseFloat(value);

                    resultInt = Math.round(num);
                    resultString = String.valueOf(resultInt);

                    return resultString;

                } catch (Exception e) {
                    return value;
                }
            }
            else {
                return value;
            }
        }

        return null;
    }

    public String code() {
        return code;
    }


    public void DEBUG_display_all_entities() {

        int counter = 0;

        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            System.out.println(counter + " | " + key + " = " + value);

            counter++;
        }
    }





}
