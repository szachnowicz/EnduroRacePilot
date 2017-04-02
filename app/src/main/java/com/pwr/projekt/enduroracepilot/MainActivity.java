package com.pwr.projekt.enduroracepilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pwr.projekt.enduroracepilot.activities.MapsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMapActivity(View view) {
        Intent mapActivity = new Intent(this, MapsActivity.class);
        startActivity( mapActivity);

    }
}
