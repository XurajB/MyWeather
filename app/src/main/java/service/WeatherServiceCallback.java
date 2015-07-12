package service;

import model.WeatherData;

/**
 * Created by suraj bhattarai on 7/10/15.
 * Callback interface to send successful API call or any failure
 */
public interface WeatherServiceCallback {
    /**
     * Handle when successfully received information from remote API call
     * @param data WeatherData
     */
    void onSuccess(WeatherData data);

    /**
     * Handle when error is received back from API call or any exception is caught
     * @param message Error message
     */
    void onFailure(String message);
}
