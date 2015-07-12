package service;

import model.WeatherData;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public interface WeatherServiceCallback {
    void onSuccess(WeatherData data);
    void onFailure(String message);
}
