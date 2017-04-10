package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.MarkerOptions;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.PointsOfRouteFragment;
import com.pwr.projekt.enduroracepilot.fragments.RouteCreationFragment;
import com.pwr.projekt.enduroracepilot.interfaces.OnMarkerAddedCallback;

import java.util.ArrayList;

public class RouteCreatingActivity extends AppCompatActivity implements OnMarkerAddedCallback {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String ROUTE_ID_REFERENCE_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_creating);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ROUTE_ID_REFERENCE_KEY = extras.getString(AddingRouteActivity.ROUTE_ID);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), ROUTE_ID_REFERENCE_KEY);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        setUpOnChangePageListner();

    }

    @Override
    public void passMarketList(ArrayList<MarkerOptions> markersList) {

//        Toast.makeText(this, "ABCD " + markersList.size(), Toast.LENGTH_SHORT).show();
//        pointsOfRouteFragment.updateList();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_route_creating, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment routeCreationFragment;

        private Fragment pointsOfRouteFragment;

        public SectionsPagerAdapter(FragmentManager fm, String ROUTE_ID_REFERENCE_KEY) {

            super(fm);
            pointsOfRouteFragment = PointsOfRouteFragment.newInstance(ROUTE_ID_REFERENCE_KEY);
            routeCreationFragment = RouteCreationFragment.newInstance(ROUTE_ID_REFERENCE_KEY);

        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return routeCreationFragment;
            }
            else {
                return pointsOfRouteFragment;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Punkty";

            }
            return null;
        }
    }
}
