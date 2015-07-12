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


public class MyWeatherActivity extends AppCompatActivity implements WeatherServiceCallback{

    private ImageView mImageViewConditionIcon;
    private TextView mTextViewTemperature;
    private TextView mTextViewCondition;
    private TextView mTextViewLocation;

    private ArrayList<Weather> mForecastData;

    private ProgressDialog mProgressDialog;

    private static final String DEFAULT_LOCATION = "Chicago, IL";
    private String mCustomLocation, mTempLocation = DEFAULT_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        mImageViewConditionIcon = (ImageView)findViewById(R.id.weather_activity_image_view_condition_icon);
        mTextViewTemperature = (TextView)findViewById(R.id.weather_activity_text_view_temperature);
        mTextViewCondition = (TextView)findViewById(R.id.weather_activity_text_view_condition);
        mTextViewLocation = (TextView)findViewById(R.id.weather_activity_text_view_location);

        ActionBar bar  = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setIcon(R.mipmap.ic_launcher);

        loadWeatherData(DEFAULT_LOCATION);

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

                // Create and show the dialog.
                DialogFragment newFragment = ForecastDialog.newInstance(mForecastData, mCustomLocation);
                newFragment.show(ft, "dialog");
            }
        });
    }

    private void loadWeatherData(String location) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.show();

        WeatherService mWeatherService = new WeatherService(this);
        mWeatherService.getWeatherData(location);
    }

    @Override
    public void onSuccess(WeatherData weatherData) {
        mCustomLocation = mTempLocation;
        if (mProgressDialog.isShowing()) mProgressDialog.hide();

        try {
            new DownloadIconTask(weatherData.getCurrentCondition().getIconUrl(), mImageViewConditionIcon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mTextViewTemperature.setText(weatherData.getCurrentCondition().getTemperature() + "° F");
        mTextViewCondition.setText(weatherData.getCurrentCondition().getDescription());
        mTextViewLocation.setText(weatherData.getRequest().getLocationQuery());
        mForecastData  = weatherData.getWeatherList();

        // Update the title bar with current location
        setTitle(" " + mCustomLocation);

    }

    @Override
    public void onFailure(String message) {
        if (mProgressDialog.isShowing()) mProgressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openEditLocation();
                return true;
            case R.id.action_refresh:
                loadWeatherData(mCustomLocation);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openEditLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Location");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputLocation = input.getText().toString();
                if (inputLocation.length()>0) {
                    loadWeatherData(inputLocation);
                    mTempLocation = inputLocation;
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid location", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Converts DP values to Pixels
    private int convertDpToPx(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getResources().getDisplayMetrics());
    }
}
