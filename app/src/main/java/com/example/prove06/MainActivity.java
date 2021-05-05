package com.example.prove06;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGetCurrentTempClick(View view) {

        WeakReference<Activity> mainActivityWeakRef = new WeakReference<Activity>(this);

        EditText text = findViewById(R.id.txt_city);
        String city = text.getText().toString();

        String requestType = this.getText(R.string.arg_current_temp).toString();

        Runnable weatherRequester = new WeatherRequester(mainActivityWeakRef, city, requestType);
        Thread thread = new Thread(weatherRequester, "thread_current_temp");

        thread.start();
    }

    public void onGetForecastClick(View view) {
        WeakReference<Activity> mainActivityWeakRef = new WeakReference<Activity>(this);

        EditText text = findViewById(R.id.txt_city);
        String city = text.getText().toString();

        String requestType = this.getText(R.string.arg_forecast).toString();

        Runnable weatherRequester = new WeatherRequester(mainActivityWeakRef, city, requestType);
        Thread thread = new Thread(weatherRequester, "thread_forecast");

        thread.start();
    }
}
