package model;

import org.json.JSONObject;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public class Weather implements DataPopulator {

    private String mDate;
    private int mMaxTemp;
    private int mMinTemp;
    private Hourly mHourly;

    public String getDate() {
        return mDate;
    }

    public int getMaxTemp() {
        return mMaxTemp;
    }

    public int getMinTemp() {
        return mMinTemp;
    }

    public Hourly getHourly() {
        return mHourly;
    }

    @Override
    public void populateData(JSONObject data) {
        mDate = data.optString("date");
        mMaxTemp = data.optInt("maxtempF");
        mMinTemp = data.optInt("mintempF");

        mHourly = new Hourly();
        mHourly.populateData(data.optJSONArray("hourly").optJSONObject(0));
    }
}
