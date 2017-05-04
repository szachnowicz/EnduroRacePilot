package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pwr.projekt.enduroracepilot.MVP.presenter.RidePresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.RideView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.map.RideFragment;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.List;

import butterknife.ButterKnife;

public class RideActivity extends AppCompatActivity implements RideView {

    private FragmentManager fragmentManager;
    private RideFragment rideFragment;
    private FragmentTransaction transaction;
    private RidePresenter ridePresenter;
    private String ROUTE_ID;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ROUTE_ID = extras.getString(AddingSingelRouteActivity.ROUTE_ID);
        }
        ButterKnife.bind(this);
        settingMapFraments();
        ridePresenter.getRoute(ROUTE_ID);
    }

    private void settingMapFraments() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        rideFragment = new RideFragment();

        transaction.add(R.id.frameLayoutForRide, rideFragment);
        transaction.commit();
        ridePresenter = new RidePresenter(this);
        rideFragment.setPresenter(ridePresenter);

    }

    @Override
    public void displayRouteDetalis(List<Route> list) {
        route = list.get(0);
        rideFragment.passRoute(route);
    }

}
