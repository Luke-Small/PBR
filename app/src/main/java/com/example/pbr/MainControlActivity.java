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

    //holds the value of the individual properties
    private double tempSeekValue;
    private int lightSeekValue, timeSeekValue;


    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    int temp;
    //Instantiate seek bar objects
    private SeekBar seekTemp;
    private SeekBar seekLight;
    private SeekBar seekTime;
    private double meterValue;

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

        //Create YieldView object and prepare for manipulation
        YieldView yourYield;
        yourYield = (YieldView)findViewById(R.id.custView);

        //allocate textViews to their ids xml layout
         mDataText = (TextView)findViewById(R.id.data); //?

        //Output TextViews and their layouts
        TextView txtTempOutput = (TextView) findViewById(R.id.tempDigitalOutput);
        TextView txtLightIntensity = (TextView)findViewById(R.id.lightIntensityOutput);
        TextView txtAmountLight = (TextView)findViewById(R.id.txtAmountLightHours);
        //set the textview and yieldview metervalue to their current seekbar progress
        txtTempOutput.setText(Integer.toString(seekTemp.getProgress()));
        txtLightIntensity.setText(Integer.toString(seekLight.getProgress()));
        txtAmountLight.setText(Integer.toString(seekTime.getProgress()));
        yourYield.setMeterVal(getMeterValue(seekTemp.getProgress(), seekLight.getProgress(), seekTime.getProgress()));

         mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "localhost:8000/lets.html");
         startDownload();


        /**
         * Event Handler for temperature seekbar
          */
        seekTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                int progressInteger = progress; //make a integer copy of progress
                Log.d("This is the progress", Integer.toString(progress));
                double progressDouble = (double)progress;
                Log.d("This pr..Double ", Double.toString(progressDouble));
                       progressDouble = progressDouble/100;
                       Log.d("after division of 100 ", Double.toString(progressDouble));
                txtTempOutput.setText(Double.toString(progressDouble));
                meterValue = getMeterValue(progressDouble, lightSeekValue, timeSeekValue);
                Log.d("meterValue = ", Double.toString(meterValue));
                yourYield.setMeterVal(meterValue);
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
                meterValue = getMeterValue(tempSeekValue, progress, timeSeekValue);
                yourYield.setMeterVal(meterValue);
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

                txtAmountLight.setText(Integer.toString(progress));
                meterValue = getMeterValue(tempSeekValue, lightSeekValue, progress);
                yourYield.setMeterVal(meterValue);

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


    /*
       Method determines if seekbar values have changed, then changes the old
       value with the new value.
     */
    private double getMeterValue(double newTempSeek, int newLightSeek, int newTimeSeek){
        if(newTempSeek != tempSeekValue)
            tempSeekValue = newTempSeek;
        if(newLightSeek != lightSeekValue)
            lightSeekValue = newLightSeek;
        if(newTimeSeek != timeSeekValue)
            timeSeekValue = newTimeSeek;
        double meterValue = calcMeterValue();
        return meterValue;
    }

    /*
      Changes the yield view value if depending on seek listeners changes
     */
    private double calcMeterValue(){
        int meterValue;         //On a scale of 0 to 240 *check this
        final int dividend = 240/3;                             //individual property weight on the meter... so 80
        final int div = dividend/10; //divide the individual weight by ten to represent the 10 grams

        double tempValue;
        double lightValue;
        double timeValue;

        //temperature parabola variables
        float[] vertex = new float[] {35, 10};  //vertex of parabola held in array literal - optimal yield parameters
        double y;   //what we find to find which is yield in grams
        double p = -0.05;          //represents distance between the vertex and the focal point;
        //float[] focus = new float[2];
        //double directrix;
        final int tempPeak = 35;                                       //degrees celsius where growth is optimal

        float gramsTotal;                                                        //total amount of grams produced
       //equation of vertical parabola opening down ----- y=-1/2(x-h)^2 + 10
        double tempGrams = p*Math.pow((tempSeekValue-vertex[0]), 2)+vertex[1];
        tempValue = tempGrams * div;   //multiply the weight of each gram relative to the meter value by the amount of grams present

        //light intensity variables
        final double lightPeak = 900.00;                                  //PAR - light intensity for now just a 0-100 scale
        final double light_slope = 10/lightPeak;
        double lightGrams;
        //linear equation for light intensity //point slope form "(y-y^1)=m(x-x^1)"  slope intercept form "y=mx+b"
        lightGrams=light_slope*(lightSeekValue-lightPeak)+10;
        lightValue = lightGrams * div;


        //light cycle variables
        final double timePeak = 86400;                               //max amount of light via seconds in a day
        final double time_slope = 10/timePeak;
        //linear equation for light cycle
        double timeGrams;
        timeGrams=time_slope*(timeSeekValue-timePeak)+10;
        timeValue = timeGrams * div;

        //add all values together to get the total value
        double sumValue = tempValue + lightValue + timeValue;
        if(sumValue < 0){ //prevents the bar from going below zero
            sumValue = 0;
        }
      /*  Log.d("time_slope = ", Double.toString(time_slope));
        Log.d("light_slope = ", Double.toString(light_slope));
        //check to insure all is working
        Log.d("grams based on temp", Double.toString(tempGrams));
        Log.d("grams based on light", Double.toString(lightGrams));
        Log.d("grams based on time", Double.toString(timeGrams));
        Log.d("which makes the sum", Double.toString(sumValue));*/
        meterValue = (int)sumValue;
      //  Log.d("meterval convert to int", Double.toString(meterValue));
        return meterValue;
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

