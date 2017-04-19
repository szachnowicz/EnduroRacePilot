package com.pwr.projekt.enduroracepilot.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.RoutePointsAdapter;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;
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

        addingAdapterListener();
        return view;

    }

    private void addingAdapterListener() {

        pointsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Database().getRefereceToObject(Route.TABEL_NAME, points.get(position).getRouteID())
                                .removeValue();
                        points.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

    }

    private void getAllPointsFromDatabase(String child, final String key) {
        new Database().readDataContinulsly(child, new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

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

            }
        });

    }

}
