package service;

import android.net.Uri;
import android.os.AsyncTask;

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
 * Created by surajbhattarai on 7/10/15.
 */
public class WeatherService {
    private WeatherServiceCallback mWeatherServiceCallback;

    private static final String ICON_URL = "http://l.yimg.com/a/i/us/we/52/%s.gif";
    private static final String API_KEY = "b315fed477ad4084a7f848f218956";

    private static final String API_ENDPOINT = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=%s&q=%s&num_of_days=5&tp=24&format=json";

    public WeatherService(WeatherServiceCallback weatherServiceCallback) {
        this.mWeatherServiceCallback = weatherServiceCallback;
    }

    public void getWeatherData(final String location) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
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

                    return data.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result == null) {
                    mWeatherServiceCallback.onFailure("Connection to server failed");
                } else {
                    try {
                        JSONObject query = new JSONObject(result);
                        JSONObject data = query.optJSONObject("data");

                        JSONArray error = data.optJSONArray("error");
                        if (error!=null) {
                            mWeatherServiceCallback.onFailure(error.optJSONObject(0).optString("msg"));
                            return;
                        }
                        WeatherData weatherData = new WeatherData();
                        weatherData.populateData(data);
                        mWeatherServiceCallback.onSuccess(weatherData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mWeatherServiceCallback.onFailure(e.getMessage());
                    }
                }
            }
        }.execute(location);
    }
}
