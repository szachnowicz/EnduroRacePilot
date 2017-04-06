package com.pwr.projekt.enduroracepilot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pwr.projekt.enduroracepilot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointsOfRouteFragment extends Fragment {

    public PointsOfRouteFragment() {

    }

    public static Fragment newInstance() {
        PointsOfRouteFragment fragment = new PointsOfRouteFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_points_of_route, container, false);
    }
}
