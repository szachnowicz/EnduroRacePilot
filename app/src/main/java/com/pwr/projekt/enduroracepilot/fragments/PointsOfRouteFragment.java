package com.pwr.projekt.enduroracepilot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.RoutePointsAdapter;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.PointsDao;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointsOfRouteFragment extends Fragment {

    private PointsDao pointsDao;
    private ListView pointsListView;
    private View view;
    private ArrayList<Point> points;
    private String ROUTE_ID_REFERENCE_KEY;

    private RoutePointsAdapter adapter;

    public PointsOfRouteFragment() {

    }

    public static Fragment newInstance(String _ROUTE_ID_REFERENCE_KEY) {
        PointsOfRouteFragment fragment = new PointsOfRouteFragment();
        fragment.setRouteRefernce(_ROUTE_ID_REFERENCE_KEY);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.notifyDataSetChanged();

//        pointsDao.getAllPointsQuery(ROUTE_ID_REFERENCE_KEY).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for (DataSnapshot chlid :
//                        children) {
//                    Point value = chlid.getValue(Point.class);
//                    if (value.getRouteID().equals(ROUTE_ID_REFERENCE_KEY) && !points.contains(value)) {
//                        points.add(value);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        getAllPointsFromDatabase(Point.TABEL_NAME, ROUTE_ID_REFERENCE_KEY);

    }

    private void setRouteRefernce(String _ROUTE_ID_REFERENCE_KEY) {
        this.ROUTE_ID_REFERENCE_KEY = _ROUTE_ID_REFERENCE_KEY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_points_of_route, container, false);
//        pointsDao = new PointsDao();
        points = new ArrayList<>();

        pointsListView = (ListView) view.findViewById(R.id.pointsListView);
        adapter = new RoutePointsAdapter(getContext(), points);
        pointsListView.setAdapter(adapter);
        return view;
    }

    private void getAllPointsFromDatabase(String child, final String key) {
        new Database().readDataContinulsly(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot chlid : children
                        ) {
                    Point value = chlid.getValue(Point.class);
                    if (value != null && value.getRouteID().equals(key) && !points.contains(value)) {
                        points.add(value);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

}
