package com.pwr.projekt.enduroracepilot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.SharedPref;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPref sharedPref= new SharedPref(getApplicationContext());

        sharedPref.getUserId();


    }

    public void showRoute(View view) {
        Intent mapActivity = new Intent(this, BrowseRouteActivity.class);
        startActivity(mapActivity);

    }



    public void goToMapFragmentActivity(View view) {
        Intent mapActivity = new Intent(this, ChooseRouteActivity.class);
        startActivity(mapActivity);

    }

    @OnClick(R.id.rideButton)
    public void goToRideActivity(View view) {

        Intent mapActivity = new Intent(this, ChooseRouteActivity.class);
        mapActivity.putExtra("RIDE", true);
        startActivity(mapActivity);

    }
}
