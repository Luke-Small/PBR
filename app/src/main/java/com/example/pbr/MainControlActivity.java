package com.example.pbr;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

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
    //Instantiate seek bars
    private SeekBar seekTemp;
    private SeekBar seekLight;
    private SeekBar seekTime;
    // Instantiate maluable textviews
   // private TextView txtTempOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control2);
        //allocate seekbars to their ids xml layout
         seekTemp = (SeekBar)findViewById(R.id.temp);  //seekbar for temperature control
         seekLight = (SeekBar)findViewById(R.id.light); //seekbar for light intensity
         seekTime = (SeekBar)findViewById(R.id.time);  //seekbar for amount of light per day

        //allocate textViews to their ids xml layout
         mDataText = (TextView)findViewById(R.id.data); //?

        //Output TextViews and their layouts
        TextView txtTempOutput = (TextView) findViewById(R.id.tempDigitalOutput);
        TextView txtLightIntensity = (TextView)findViewById(R.id.lightIntensityOutput);
        TextView txtAmountLight = (TextView)findViewById(R.id.txtAmountLightHours);
        //set the textview to their current seekbar progress
        txtTempOutput.setText(Integer.toString(seekTemp.getProgress()));
        txtLightIntensity.setText(Integer.toString(seekLight.getProgress()));
        txtAmountLight.setText(Integer.toString(seekTime.getProgress()));
        //Custom View which has no known application yeat we shall figure that out later
        YieldView yourYield;
        yourYield = (YieldView)findViewById(R.id.custView);
         mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "localhost:8000/lets.html");
         startDownload();


        /**
         * Event Handler for temperature seekbar
          */
        seekTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                txtTempOutput.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                //Maybe highlight the seek bar or brighten its hue
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //A toast will appear to show you the changed value
                Toast.makeText(MainControlActivity.this, "Temperature Changed :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Event Handler for light intensity seekbar
         */
        seekLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                txtLightIntensity.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainControlActivity.this, "Light Intensity increased by :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Event Handler for light timer seekbar
         */
        seekTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                txtTempOutput.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainControlActivity.this, "Light is set for blank hours per day  :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
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

