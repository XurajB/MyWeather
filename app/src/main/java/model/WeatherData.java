package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public class WeatherData implements DataPopulator {

    private CurrentCondition mCurrentCondition;
    private Request mRequest;
    private ArrayList<Weather> mWeatherList;
    public CurrentCondition getCurrentCondition() {
        return mCurrentCondition;
    }
    public Request getRequest() {
        return mRequest;
    }
    public ArrayList<Weather> getWeatherList() {
        return mWeatherList;
    }

    @Override
    public void populateData(JSONObject data) {

        mCurrentCondition = new CurrentCondition();
        mRequest = new Request();
        mWeatherList = new ArrayList<>();

        JSONArray conditionArray = data.optJSONArray("current_condition");
        JSONArray requestArray = data.optJSONArray("request");
        JSONArray weatherList = data.optJSONArray("weather");

        mCurrentCondition.populateData(conditionArray.optJSONObject(0));
        mRequest.populateData(requestArray.optJSONObject(0));

        for (int count=0; count<weatherList.length(); count++) {
            Weather weather = new Weather();
            weather.populateData(weatherList.optJSONObject(count));
            mWeatherList.add(weather);
        }
    }
}
