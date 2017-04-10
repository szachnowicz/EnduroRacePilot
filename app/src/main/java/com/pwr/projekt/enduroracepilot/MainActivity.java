package com.pwr.projekt.enduroracepilot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pwr.projekt.enduroracepilot.activities.AddingRouteActivity;
import com.pwr.projekt.enduroracepilot.activities.CreatedRoutesListActivity;
import com.pwr.projekt.enduroracepilot.activities.EditingRouteActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void showRoute(View view) {
        Intent mapActivity = new Intent(this, CreatedRoutesListActivity.class);
        startActivity(mapActivity);

    }

    public void goToCreateRouteActivity(View view) {
        Intent mapActivity = new Intent(this, AddingRouteActivity.class);
        startActivity(mapActivity);

    }

    public void goToMapFragmentActivity(View view) {
        Intent mapActivity = new Intent(this, EditingRouteActivity.class);
        startActivity(mapActivity);



    }
}
