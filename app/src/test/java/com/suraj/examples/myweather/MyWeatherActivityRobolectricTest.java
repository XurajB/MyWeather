package com.suraj.examples.myweather;

import android.os.Build;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by surajbhattarai on 9/14/15.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MyWeatherActivityRobolectricTest {
    private MyWeatherActivity myWeatherActivity;
    private ActivityController<MyWeatherActivity> myActivityController;

    @Before
    public void setup() {
        myWeatherActivity = Robolectric.setupActivity(MyWeatherActivity.class);
        myActivityController = Robolectric.buildActivity(MyWeatherActivity.class);

    }

    @Test
    public void validateTextViewText() {
        TextView tv = (TextView)myWeatherActivity.findViewById(R.id.textView);
        assertNotNull("TextView could not be found", tv);
        assertTrue("TextView contains incorrect text", "Current Condition".equals(tv.getText().toString()));
    }

    @Test
    public void pausesAndResumesActivity() {

        myActivityController.pause().resume();

        TextView tv = (TextView)myWeatherActivity.findViewById(R.id.textView);
        assertNotNull("TextView could not be found", tv);
        assertTrue("TextView contains incorrect text", "Current Condition".equals(tv.getText().toString()));
    }

}
