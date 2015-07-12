package service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.suraj.examples.myweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import model.WeatherData;

/**
 * Created by suraj bhattarai on 7/10/15.
 * This class gets weather data from the World Weather Online API using unique API KEY.
 */
public class WeatherService {
    private WeatherServiceCallback mWeatherServiceCallback;
    private Context mContext;

    /** Unique API key and End point URL */
    private static final String API_KEY = "b315fed477ad4084a7f848f218956";
    private static final String API_ENDPOINT = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=%s&q=%s&num_of_days=5&tp=24&format=json";

    /** Constructor to pass a callback object and context */
    public WeatherService(WeatherServiceCallback weatherServiceCallback, Context context) {
        this.mWeatherServiceCallback = weatherServiceCallback;
        this.mContext = context;
    }

    /**
     * Get weather data from API using worker thread. Open URL connection and fetch data line by line
     * @param location location
     */
    public void getWeatherData(final String location) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                /** Combine API URL, API key and location into one URL */
                String apiUrl = String.format(API_ENDPOINT,Uri.encode(API_KEY),Uri.encode(location));
                try {
                    URL endPointUrl = new URL(apiUrl);
                    URLConnection urlConnection = endPointUrl.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                    StringBuilder data = new StringBuilder();
                    String readLine;
                    while ((readLine = bufferedReader.readLine()) != null) {
                        data.append(readLine);
                    }
                    /** Close connections */
                    inputStream.close();
                    bufferedReader.close();
                    /** Return weather data */
                    return data.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                /** Program returns null only when we receive
                 * exception getting data above */
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                /** Send error message to the UI if we get null data */
                if (result == null) {
                    mWeatherServiceCallback.onFailure(mContext.getString(R.string.connection_failed_text));
                } else {
                    try {
                        /** Parse JSON data */
                        JSONObject query = new JSONObject(result);
                        JSONObject data = query.optJSONObject("data");
                        /** API sends back an error object in case of any exception. If we receive
                         * an error object - we pass the error message back to the Activity using callback object */
                        JSONArray error = data.optJSONArray("error");
                        if (error!=null) {
                            mWeatherServiceCallback.onFailure(location+ " "+ error.optJSONObject(0).optString("msg"));
                            return;
                        }
                        /** If no error - create a WeatherData object and populate */
                        WeatherData weatherData = new WeatherData();
                        weatherData.populateData(data);
                        mWeatherServiceCallback.onSuccess(weatherData);

                    } catch (JSONException e) {
                        /** In case of any exception, send the message back to Activity */
                        e.printStackTrace();
                        mWeatherServiceCallback.onFailure(e.getMessage());
                    }
                }
            }
        }.execute(location);
    }
}