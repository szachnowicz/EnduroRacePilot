package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.pwr.projekt.enduroracepilot.MVP.presenter.RidePresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.RideView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.FragmentForGPSSetting;
import com.pwr.projekt.enduroracepilot.fragments.map.RideFragment;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideActivity extends AppCompatActivity implements RideView {

    @BindView(R.id.frameLayoutForRide)
    FrameLayout frameLayoutMap;
    @BindView(R.id.frameLayoutForGpsSetting)
    FrameLayout frameLayoutGPSSetting;
    private FragmentManager fragmentManager;
    private RideFragment rideFragment;
    private FragmentForGPSSetting fragmentForGPSSetting;
    private FragmentTransaction transaction;
    private RidePresenter ridePresenter;
    private String ROUTE_ID;
    private Route route;
    private boolean settingVisible = false;

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
        fragmentForGPSSetting = new FragmentForGPSSetting();

        transaction.add(R.id.frameLayoutForRide, rideFragment);
        transaction.add(R.id.frameLayoutForGpsSetting, fragmentForGPSSetting);

        transaction.commit();
        frameLayoutGPSSetting.setVisibility(View.GONE);

        ridePresenter = new RidePresenter(this);
        rideFragment.setPresenter(ridePresenter);

    }

    @OnClick(R.id.gpsOptionButton)
    public void gpsOptionButton(View view) {
        frameLayoutGPSSetting.setVisibility(View.VISIBLE);

        if (settingVisible) {
            frameLayoutGPSSetting.setVisibility(View.GONE);
            frameLayoutMap.setVisibility(View.VISIBLE);
            settingVisible = !settingVisible;
            rideFragment.setSharePref();
        }
        else {
            frameLayoutGPSSetting.setVisibility(View.VISIBLE);
            frameLayoutMap.setVisibility(View.GONE);
            settingVisible = !settingVisible;
            rideFragment.setSharePref();

        }

    }

    @Override
    public void displayRouteDetalis(List<Route> list) {
        route = list.get(0);
        rideFragment.passRoute(route);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    double poiD = ridePresenter.getPoiDetectionDistance();
                    poiD++;
                    ridePresenter.setPoiDetectionDistance(poiD);
                    ridePresenter.setDistanceToPath(poiD);
                    Snackbar.make(frameLayoutMap, "Poi wykrywane " + poiD + "m od lokal izacji  z gps", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    double poiD = ridePresenter.getPoiDetectionDistance();
                    poiD--;
                    ridePresenter.setPoiDetectionDistance(poiD);

                    Snackbar.make(frameLayoutMap, "Poi wykrywane " + poiD + "m od lokal izacji  z gps", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
