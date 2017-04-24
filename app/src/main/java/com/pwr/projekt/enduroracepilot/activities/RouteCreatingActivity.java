package com.pwr.projekt.enduroracepilot.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.SectionsPagerAdapter;
import com.pwr.projekt.enduroracepilot.fragments.RouteDetalisFragment;
import com.pwr.projekt.enduroracepilot.fragments.RouteDisplayingMapFragment;
import com.pwr.projekt.enduroracepilot.interfaces.MapDisplayingCallback;
import com.pwr.projekt.enduroracepilot.interfaces.RouteDetalisCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteCreatingActivity extends AppCompatActivity implements RouteDetalisCallback, MapDisplayingCallback {

    @BindView(R.id.container)
    ViewPager mViewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String ROUTE_ID_REFERENCE_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_creating);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ROUTE_ID_REFERENCE_KEY = extras.getString(AddingSingelRouteActivity.ROUTE_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), ROUTE_ID_REFERENCE_KEY);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void itemOnListCliked(Point point) {
        mViewPager.setCurrentItem(0);
        RouteDisplayingMapFragment item = (RouteDisplayingMapFragment) mSectionsPagerAdapter.getItem(0);
        item.zoomMapToPoint(point);
    }

    @Override
    public void passTheRouteMapFragment(Route route) {
        if (route != null) {

            RouteDisplayingMapFragment item = (RouteDisplayingMapFragment) mSectionsPagerAdapter.getItem(0);
            item.passTheRoute(route);

        }
    }

    @Override
    public void passRouteToDetalisFragment(Route route) {
        RouteDetalisFragment routeD = (RouteDetalisFragment) mSectionsPagerAdapter.getItem(1);
        routeD.setRoute(route);
    }

    @Override
    public void onBackPressed() {

        showAsForSaveRouteDialog();
    }

    private void showAsForSaveRouteDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save route");
        alert.setMessage("Would you like to sava the route ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RouteDetalisFragment routeD = (RouteDetalisFragment) mSectionsPagerAdapter.getItem(1);
                routeD.saveRoute();
                finish();
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });

        alert.show();
    }
}






