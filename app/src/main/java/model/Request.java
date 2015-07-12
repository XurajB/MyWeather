package model;

import org.json.JSONObject;

/**
 * Created by surajbhattarai on 7/10/15.
 */
public class Request implements DataPopulator {

    private String mLocationQuery;

    public String getLocationQuery() {
        return mLocationQuery;
    }

    @Override
    public void populateData(JSONObject data) {
        mLocationQuery = data.optString("query");
    }
}
