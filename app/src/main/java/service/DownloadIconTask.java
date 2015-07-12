package service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.suraj.examples.myweather.R;

import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by surajbhattarai on 7/11/15.
 */
public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
    ImageView iconImageView;

    public DownloadIconTask(String iconUrl, ImageView iconView) throws MalformedURLException {
        this.iconImageView = iconView;
        iconImageView.setImageResource(R.drawable.loading);
        execute(iconUrl);
    }

    protected Bitmap doInBackground(String... args) {

        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(args[0]).openStream();
            mIcon = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        iconImageView.setImageBitmap(result);
    }
}