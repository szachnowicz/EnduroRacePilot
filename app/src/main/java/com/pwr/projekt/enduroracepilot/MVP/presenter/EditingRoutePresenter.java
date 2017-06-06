package com.pwr.projekt.enduroracepilot.MVP.presenter;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pwr.projekt.enduroracepilot.MVP.repository.RouteRepository;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.MapCalculator;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

/**
 * Created by Sebastian on 2017-04-21.
 */

public class EditingRoutePresenter {

    private RouteRepository routeRepository;

    public EditingRoutePresenter() {

        routeRepository = new RouteRepository();
    }

    public void onMapClick(LatLng latLng, GoogleMap googleMap, Route route) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);

        if (route.getPointsOfRoute().size() == 0) {
            marker.title("Start");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_flag));
        }
        else {
//            marker.position(latLng).title("point nr").snippet("");
        }

        marker.draggable(true);
        googleMap.addMarker(marker);

        Point point = new Point(route.getRouteID(), route.getPointsOfRoute().size(), marker);
        route.getPointsOfRoute().add(point);

        contectThePoinsIntoRoute(googleMap, route);
    }

    public void contectThePoinsIntoRoute(GoogleMap googleMap, Route route) {

        if (route.getPointsOfRoute().size() < 1)
            return;

        googleMap.clear();
        PolylineOptions options = new PolylineOptions();

        for (Point point : route.getPointsOfRoute()
                ) {
            MarkerOptions marek = new MarkerOptions();
            marek.position(point.getLatLng());
//            marek.title(point.getPointID() + "");

        //    googleMap.addMarker(marek);
            if (!options.getPoints().contains(point))
                options.add(point.getLatLng());
        }
        options.width(6).color(Color.RED);
        Polyline line = googleMap.addPolyline(options);

    }

    public void addNewPointFromGpsToRoute(LatLng latLng, GoogleMap mGoogleMap, Route route) {
        // checking that the the last added point is in the distance form current latlng for gps


        if (route.getPListSize() == 0) {
            handleFirstPoint(latLng, mGoogleMap, route);

        }
        double maxDistance = 2;
        Point point_ = route.getPoint(route.getPListSize() - 1);
        double distanceFromPointToLnglat = MapCalculator.getDistanceFromPointToLnglat(point_, latLng);

//        if (route.getPListSize() > 0 && distanceFromPointToLnglat > maxDistance) {
            handleFirstPoint(latLng, mGoogleMap, route);
            contectThePoinsIntoRoute(mGoogleMap, route);
//        }



    }

    private void handleFirstPoint(LatLng latLng, GoogleMap mGoogleMap, Route route) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);

        if (route.getPointsOfRoute().size() == 0) {
            marker.title("Start");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_flag));
        }



        Point point = new Point(route.getRouteID(), route.getPListSize(), marker);
        route.getPointsOfRoute().add(point);
        mGoogleMap.addMarker(marker);
    }
}
