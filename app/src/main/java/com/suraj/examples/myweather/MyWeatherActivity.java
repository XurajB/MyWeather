/*
 * Copyright (C) 2015 Suraj Bhattarai
 *
 */
package com.suraj.examples.myweather;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

import model.Weather;
import model.WeatherData;
import service.DownloadIconTask;
import service.WeatherService;
import service.WeatherServiceCallback;

/**
 * Main activity for the application. It sets the UI for the user to interact with
 * The weather information is received from the WeatherService class by passing location.
 * Implements WeatherServiceCallback interface to indicate/update UI for successful or
 * failed operations.
 */
public class MyWeatherActivity extends AppCompatActivity implements WeatherServiceCallback{

    /** UI elements */
    private ImageView mImageViewConditionIcon;
    private TextView mTextViewTemperature;
    private TextView mTextViewCondition;
    private TextView mTextViewLocation;
    private TextView mTextViewPrecipitation;

    /** ArrayList to store forecast data */
    private ArrayList<Weather> mForecastData;

    /** Progress Dialog to display when the WeatherService is busy */
    private ProgressDialog mProgressDialog;

    /** Default location is set to Chicago, IL */
    private static final String DEFAULT_LOCATION = "Chicago, IL";
    private static final String KEY_LOCATION = "CurrentLocation";

    private String mCurrentLocation, mTempLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        /** Instantiate UI elements */
        mImageViewConditionIcon = (ImageView)findViewById(R.id.weather_activity_image_view_condition_icon);
        mTextViewTemperature = (TextView)findViewById(R.id.weather_activity_text_view_temperature);
        mTextViewCondition = (TextView)findViewById(R.id.weather_activity_text_view_condition);
        mTextViewLocation = (TextView)findViewById(R.id.weather_activity_text_view_location);
        mTextViewPrecipitation = (TextView)findViewById(R.id.weather_activity_text_view_precipitation);

        /** Set application icon on the action bar */
        ActionBar bar  = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setIcon(R.mipmap.ic_launcher);

        /** Save the current location in Bundle so it can be retrieved after orientation change */
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getString(KEY_LOCATION);
            mTempLocation = mCurrentLocation;
        } else {
            mCurrentLocation = DEFAULT_LOCATION;
            mTempLocation = DEFAULT_LOCATION;
        }
        loadWeatherData(mCurrentLocation);

        /** Make the entire layout clickable so the user can tap anywhere to get the forecast information */
        RelativeLayout mLayout = (RelativeLayout)findViewById(R.id.weather_activity_relative_layout_main);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                /** Create and show the dialog. */
                DialogFragment newFragment = ForecastDialog.newInstance(mForecastData, mCurrentLocation);
                newFragment.show(ft, "dialog");
            }
        });
    }

    /**
     * Load weather data from API
     * @param location user entered location
     */
    private void loadWeatherData(String location) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_message));
        mProgressDialog.show();
        /** Call the weather service for weather data */
        WeatherService mWeatherService = new WeatherService(this, this);
        mWeatherService.getWeatherData(location);
    }

    /** This method gets called when the WeatherService gets weather data successfully using API call.
     * Weather data is passed with this method. */
    @Override
    public void onSuccess(WeatherData weatherData) {
        mCurrentLocation = mTempLocation;
        if (mProgressDialog.isShowing()) mProgressDialog.hide();

        try {
            new DownloadIconTask(weatherData.getCurrentCondition().getIconUrl(), mImageViewConditionIcon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /** Update UI with received weather data */
        mTextViewTemperature.setText(weatherData.getCurrentCondition().getTemperature() + getString(R.string.fahrenheit_unit));
        mTextViewCondition.setText(weatherData.getCurrentCondition().getDescription());
        mTextViewLocation.setText(weatherData.getRequest().getLocationQuery());
        mTextViewPrecipitation.setText(getString(R.string.precipitation_title)+ weatherData.getCurrentCondition().getPrecipitation());

        mForecastData  = weatherData.getWeatherList();

        /** Update the title bar with current location */
        setTitle(" " + mCurrentLocation);

    }

    /** This method gets called when the WeatherService fails during API call.
     * Any error message associated to the failure is passed and displayed as a Toast */
    @Override
    public void onFailure(String message) {
        if (mProgressDialog.isShowing()) mProgressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        mTempLocation = mCurrentLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the menu items for use in the action bar */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /** Handle presses on the action bar items */
        switch (item.getItemId()) {
            case R.id.action_settings:
                openEditLocation();
                return true;
            case R.id.action_refresh:
                loadWeatherData(mCurrentLocation);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Open alert dialog for editing location. The WeatherService is called after new location is entered
     */
    private void openEditLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_location_title));
        /** Set up the input */
        final EditText input = new EditText(this);
        /** Specify the type of input expected */
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(mCurrentLocation);
        builder.setView(input);

        /** Set up the buttons */
        builder.setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputLocation = input.getText().toString();
                if (inputLocation.length()>0) {
                    loadWeatherData(inputLocation);
                    mTempLocation = inputLocation;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_location_text), Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /** Converts DP values to Pixels */
    private int convertDpToPx(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getResources().getDisplayMetrics());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /** Save UI state changes to the savedInstanceState. */
        savedInstanceState.putString(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}