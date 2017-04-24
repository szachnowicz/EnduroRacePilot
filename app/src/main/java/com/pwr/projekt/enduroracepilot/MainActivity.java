package com.pwr.projekt.enduroracepilot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import com.pwr.projekt.enduroracepilot.activities.AddingSingelRouteActivity;
import com.pwr.projekt.enduroracepilot.activities.BrowseRouteActivity;
import com.pwr.projekt.enduroracepilot.activities.EditingRouteActivity;
import com.pwr.projekt.enduroracepilot.activities.RouteCreatingActivity;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_entry_point);


         //TODO: start run
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());



    }

    public void showRoute(View view) {
        Intent mapActivity = new Intent(this, BrowseRouteActivity.class);
        startActivity(mapActivity);

    }
/*
    public void goToCreateRouteActivity(View view) {
        Intent mapActivity = new Intent(this, RouteCreatingActivity.class);
        startActivity(mapActivity);

    }
/*
    public void goToMapFragmentActivity(View view) {
        Intent mapActivity = new Intent(this, EditingRouteActivity.class);
        startActivity(mapActivity);

    }
    */
}
