package com.suraj.examples.myweather;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import model.Weather;


public class ForecastDialog extends DialogFragment {

    private static ArrayList<Weather> mForecast;
    private static String mCurrentLocation;

    static ForecastDialog newInstance(ArrayList<Weather> forecastData, String currentLocation) {
        ForecastDialog f = new ForecastDialog();
        mForecast = forecastData;
        mCurrentLocation = currentLocation;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecastView = inflater.inflate(R.layout.forecast_fragment, container,
                false);
        getDialog().setTitle(mCurrentLocation + " 5 Day Forecast");

        ForecastGridAdapter forecastGridAdapter = new ForecastGridAdapter(getActivity(), mForecast);

        GridView mForecastGrid = (GridView)forecastView.findViewById(R.id.weather_activity_forecast_grid);
        mForecastGrid.setAdapter(forecastGridAdapter);

        return forecastView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}
