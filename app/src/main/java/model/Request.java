package model;

import org.json.JSONObject;

/**
 * Created by suraj bhattarai on 7/10/15.
 * POJO to store Request JSON object received from the API call.
 * The request consists of formatted location query submitted during the API call.
 */
public class Request implements DataPopulator {

    /** Define variables */
    private String mLocationQuery;

    /** Define getters */
    public String getLocationQuery() {
        return mLocationQuery;
    }

    /** populate data */
    @Override
    public void populateData(JSONObject data) {
        mLocationQuery = data.optString("query");
    }
}