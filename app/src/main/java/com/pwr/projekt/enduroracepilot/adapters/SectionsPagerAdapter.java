package com.pwr.projekt.enduroracepilot.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pwr.projekt.enduroracepilot.fragments.RouteDetalisFragment;
import com.pwr.projekt.enduroracepilot.fragments.map.RouteEditingMapFragment;

/**
 * Created by Sebastian on 2017-04-18.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Fragment routeCreationFragment;

    private Fragment pointsOfRouteFragment;

    public SectionsPagerAdapter(FragmentManager fm, String ROUTE_ID_REFERENCE_KEY) {

        super(fm);
        pointsOfRouteFragment = RouteDetalisFragment.newInstance(ROUTE_ID_REFERENCE_KEY);
        routeCreationFragment = RouteEditingMapFragment.newInstance(ROUTE_ID_REFERENCE_KEY);

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