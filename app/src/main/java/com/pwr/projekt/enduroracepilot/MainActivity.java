package com.pwr.projekt.enduroracepilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pwr.projekt.enduroracepilot.activities.MapFragmentActivity;
import com.pwr.projekt.enduroracepilot.activities.RouteCreatingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToCreateRouteActivity(View view) {
        Intent mapActivity = new Intent(this, RouteCreatingActivity.class);
        startActivity( mapActivity);

    }

    public void goToMapFragmentActivity(View view) {
        Intent mapActivity = new Intent(this, MapFragmentActivity.class);
        startActivity( mapActivity);

    }
}
