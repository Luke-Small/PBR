package com.example.pbr;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.view.View;
import java.io.IOException;
import java.net.MalformedURLException;

public class MainControlActivity extends FragmentActivity implements DownloadCallback{
    private String TAG = MainActivity.class.getSimpleName();
    // Keep a reference to the NetworkFragment which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;
    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    // Reference to the TextView showing fetched data, so we can clear it with a button
    // as necessary.
    private TextView mDataText;
    private TextView txtTemp;

    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    int temp;

    private SeekBar seekTemp; // = (ProgressBar)findViewById(R.id.temp); // Initiate progress bar for temperature setting/reading and assign it to temp progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control2);
         seekTemp = (SeekBar)findViewById(R.id.temp);
         txtTemp = (TextView)findViewById(R.id.tempDigitalInput);
         mDataText = (TextView)findViewById(R.id.data);
         mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "localhost:8000/lets.html");
         startDownload();
         YieldView yourYield;
         yourYield = (YieldView)findViewById(R.id.custView);
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment!= null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {

        if (result != null) {
             try {
                  // JSONArray JA = new JSONArray(result);
                     JSONObject JO = new JSONObject(result);
                     mDataText.setText(result);
                 // for (int i = 0; i < JA.length(); i++) {
                 //      JSONObject JO = (JSONObject) JA.get(i);
                 /*singleParsed = "Name: " + JO.get("Name") + "\n" +
                         "Temperature: " + JO.get("Air Temperature") + "\n" +
                         "Date: " + JO.get("Date") + "\n" +
                         "Light Start: " + JO.get("Light Start") + "\n";
                 dataParsed = dataParsed + singleParsed + "\n";*/
                temp = JO.getInt("Air Temperature");
                 mDataText.setText("lskfjs");
                 seekTemp.setProgress(temp);
                 txtTemp.setText(Integer.toString(temp) + "degrees Celsius");

             } catch (final JSONException e){
                 mDataText.setText("JSON parsing error");
                 Log.e(TAG, "Json parsing error:  " + e.getMessage());
             }

        }
         else{
                           mDataText.setText(getString(R.string.connection_error));
                       }

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                mDataText.setText("" + percentComplete + "%");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }
}

