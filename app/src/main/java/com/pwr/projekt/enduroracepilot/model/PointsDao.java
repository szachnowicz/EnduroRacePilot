package com.pwr.projekt.enduroracepilot.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.ArrayList;

/**
 * Created by Sebastian on 2017-04-09.
 */

public class PointsDao {

    private final DatabaseReference routeRefernce;
    private ArrayList<Point> points;

    public PointsDao() {
        // REFERECE TO TABEL ROUTE
        routeRefernce = FirebaseDatabase.getInstance().getReference().child(Route.TABEL_NAME);
    }

    public DatabaseReference getRouteRefernce() {
        return routeRefernce;
    }

    public ArrayList<Point> allPointForRouteID(String routeID) {

        routeRefernce.orderByChild("routeID").equalTo(routeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot chlid :
                        children) {

                    Point value = chlid.getValue(Point.class);
                    if (!points.contains(value)) {
                        points.add(value);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return points;
    }

    public Query getAllPointsQuery(String routeID) {
        return routeRefernce.orderByChild("routeID").equalTo(routeID);

    }

}