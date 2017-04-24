package com.pwr.projekt.enduroracepilot.MVP.presenter;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pwr.projekt.enduroracepilot.MVP.repository.RouteRepository;
import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

/**
 * Created by Sebastian on 2017-04-23.
 */

public class AddPoiPresenter {

    private RouteRepository routeRepository;
    private RouteView browseRouteView;

    public AddPoiPresenter(RouteView browseRouteView) {
        this.browseRouteView = browseRouteView;
        routeRepository = new RouteRepository();
    }

    public void getRoute(String routeID) {
        routeRepository.getSingleRoute(routeID, browseRouteView);
    }

    public void drawRouteOnMap(GoogleMap googleMap, Route route) {
        if (route.getPointsOfRoute().size() < 1)
            return;

        googleMap.clear();
        PolylineOptions options = new PolylineOptions();

        for (Point point : route.getPointsOfRoute()
                ) {
            MarkerOptions marek = new MarkerOptions();
            marek.position(point.getLatLng());
            marek.title(point.getPointID() + "");

            googleMap.addMarker(marek);
            if (!options.getPoints().contains(point))
                options.add(point.getLatLng());
        }
        options.width(6).color(Color.RED);
        Polyline line = googleMap.addPolyline(options);

    }

    public void addAllPoiIfExist(GoogleMap googleMap, Route route) {

    }

    public void changeFocuse(GoogleMap mGoogleMap, Route route, int currentPoint, MarkerOptions currentFocuseMarker) {

        if (currentPoint < 0 || currentPoint >= route.getPListSize())
            return;

        LatLng latLng = route.getPointsOfRoute().get(currentPoint).getLatLng();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        currentFocuseMarker.position(latLng);

    }
}
