package model;

import android.provider.ContactsContract;

import org.json.JSONObject;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public class Hourly implements DataPopulator {

    private int mTemperature;
    private double mPrecipitation;
    private String mDescription;
    private String mIconUrl;

    public int getTemperature() {
        return mTemperature;
    }

    public double getPrecipitation() {
        return mPrecipitation;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    @Override
    public void populateData(JSONObject data) {
        this.mPrecipitation = data.optDouble("precipMM");
        this.mDescription = data.optJSONArray("weatherDesc").optJSONObject(0).optString("value");
        this.mIconUrl = data.optJSONArray("weatherIconUrl").optJSONObject(0).optString("value");
        this.mTemperature = data.optInt("temp_F");
    }
}
