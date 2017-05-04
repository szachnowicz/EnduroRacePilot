package com.pwr.projekt.enduroracepilot.MVP.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.MVP.repository.impl.RouteRepisotryImpl;
import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 2017-04-20.
 */

public class RouteRepository implements RouteRepisotryImpl {

    public RouteRepository() {

    }

    @Override
    public void getAllRoutes(String userID, final RouteView browseRouteView) {

        final List<Route> list = new ArrayList<>();
        new Database().readDataOnce(Route.TABEL_NAME, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snap : children
                        ) {

                    Route value = snap.getValue(Route.class);

                    list.add(value);

                }
                browseRouteView.displayRouteDetalis(list);

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    @Override
    public void getSingleRoute(final String routeID, final RouteView editingRouteView) {

        new Database().readDataOnce(Route.TABEL_NAME, new OnGetDataListener() {
            @Override
            public void onStart() {}
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snap : children
                        ) {

                    Route value = snap.getValue(Route.class);
                    if (value.getRouteID().equals(routeID))
                    {
                        List<Route> list = new ArrayList<Route>();
                        list.add(value);
                        editingRouteView.displayRouteDetalis(list);
                        break;
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {}
        });

    }

    @Override
    public void add(Route item) {
        new Database().getRefereceToObject(Route.TABEL_NAME, item.getRouteID()).push().setValue(item);
    }

    @Override
    public void add(Iterable<Route> items) {

    }

    @Override
    public void update(Route item) {
        new Database().getRefereceToObject(Route.TABEL_NAME, item.getRouteID()).setValue(item);
    }

    @Override
    public void remove(Route item) {
        new Database().getRefereceToObject(Route.TABEL_NAME, item.getRouteID()).removeValue();

    }
}
