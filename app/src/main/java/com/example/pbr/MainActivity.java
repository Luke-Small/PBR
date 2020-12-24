package com.example.pbr;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] mainMenuOptions = {"Main Controls", "Spirulina Information", "Device Configuration"};
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, R.id.travel, mainMenuOptions));
    }

    protected void onListItemClick(ListView l, View v, int position, long id){
        switch(position){
            case 0:
                startActivity(new Intent(MainActivity.this, MainControlActivity.class));
                break;
            case 1:
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, DeviceConfigActivity.class));
                break;
        }
    }
}