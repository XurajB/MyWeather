package com.suraj.examples.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;

import model.Weather;
import service.DownloadIconTask;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public class ForecastGridAdapter extends BaseAdapter {

    private Context mContext;

    private final String[] mDays;
    private final Integer[] mHighs;
    private final Integer[] mLows;
    private final String[] mDescriptions;
    private final String[] mIconUrls;
    private final Double[] mPricipations;

    public ForecastGridAdapter(Context context, ArrayList<Weather> forecasts) {
        this.mContext = context;

        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> iconUrls = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<Integer> highs = new ArrayList<>();
        ArrayList<Integer> lows = new ArrayList<>();
        ArrayList<Double> precipitations = new ArrayList<>();

        for (Weather forecast : forecasts) {
            days.add(forecast.getDate());
            highs.add(forecast.getMaxTemp());
            lows.add(forecast.getMinTemp());
            iconUrls.add(forecast.getHourly().getIconUrl());
            descriptions.add(forecast.getHourly().getDescription());
            precipitations.add(forecast.getHourly().getPrecipitation());
        }

        this.mDays = days.toArray(new String[forecasts.size()]);
        this.mHighs = highs.toArray(new Integer[forecasts.size()]);
        this.mLows = lows.toArray(new Integer[forecasts.size()]);
        this.mDescriptions = descriptions.toArray(new String[forecasts.size()]);
        this.mIconUrls = iconUrls.toArray(new String[forecasts.size()]);
        this.mPricipations = precipitations.toArray(new Double[forecasts.size()]);

    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View forecastGrid;
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            forecastGrid = inflater.inflate(R.layout.forecast_single, null);
        } else {
            forecastGrid = convertView;
        }
        TextView textViewDay = (TextView)forecastGrid.findViewById(R.id.forecast_single_text_view_day);
        ImageView imageViewCondition = (ImageView)forecastGrid.findViewById(R.id.forecast_single_image_view_condition);
        TextView textViewHighLow = (TextView)forecastGrid.findViewById(R.id.forecast_single_text_view_high_low);
        TextView textViewDescription = (TextView)forecastGrid.findViewById(R.id.forecast_single_text_view_description);
        TextView textViewPrecipitation = (TextView)forecastGrid.findViewById(R.id.forecast_single_text_view_precipitation);

        textViewDay.setText(mDays[position]);
        textViewDescription.setText(mDescriptions[position]);
        textViewPrecipitation.setText("Precipitation " + mPricipations[position]+" mm");

        try {
            new DownloadIconTask(mIconUrls[position], imageViewCondition);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        textViewHighLow.setText(mHighs[position] + "°F/" + mLows[position] + "°F");

        return forecastGrid;
    }
}
