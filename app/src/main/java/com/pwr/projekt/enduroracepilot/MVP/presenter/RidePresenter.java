package com.pwr.projekt.enduroracepilot.MVP.presenter;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.pwr.projekt.enduroracepilot.MVP.repository.RouteRepository;
import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.model.MapEntity.MapCalculator;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.PoiItem;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.List;

/**
 * Created by Sebastian on 2017-04-29.
 */

public class RidePresenter {

    private RouteRepository routeRepository;
    private RouteView browseRouteView;
    private Route route;
    private List<LatLng> routeLine;

    public RidePresenter(RouteView browseRouteView) {
        this.browseRouteView = browseRouteView;
        routeRepository = new RouteRepository();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
        routeLine = route.onlyLatLngList();
    }

    public void getRoute(String routeID) {
        routeRepository.getSingleRoute(routeID, browseRouteView);
    }

    public void drawRouteOnMap(GoogleMap googleMap) {
        if (route.getPointsOfRoute().size() < 1)
            return;

        googleMap.clear();
        PolylineOptions options = new PolylineOptions();

        for (Point point : route.getPointsOfRoute()
                ) {
            MarkerOptions marek = new MarkerOptions();
            marek.position(point.getLatLng());
            marek.title(point.getPointID() + "");
            if (!options.getPoints().contains(point))
                options.add(point.getLatLng());
        }
        options.width(6).color(Color.RED);
        Polyline line = googleMap.addPolyline(options);

    }

    public void addAllPoiIfExist(GoogleMap googleMap) {
        for (PoiItem item : route.getPoiItemList()
                ) {
            MarkerOptions marker = new MarkerOptions();
            marker.draggable(true);
            marker.position(item.getLatLng());
            marker.icon(BitmapDescriptorFactory.fromResource(item.getPoi().getDrawable()));
            googleMap.addMarker(marker);

        }
    }

    public void updateCameraBering(GoogleMap googleMap, int currentPoint) {
        if (currentPoint + 1 >= route.getPListSize())
            return;

        LatLng routeFirstPoint = route.getPointsOfRoute().get(currentPoint).getLatLng();
        LatLng routeSecondPoint = route.getPointsOfRoute().get(currentPoint + 1).getLatLng();
        Location startLocation = new Location("startingPoint");
        startLocation.setLatitude(routeFirstPoint.latitude);
        startLocation.setLongitude(routeFirstPoint.longitude);

        Location endLocation = new Location("endingPoint");
        endLocation.setLatitude(routeSecondPoint.latitude);
        endLocation.setLongitude(routeSecondPoint.longitude);

        float targetBearing = startLocation.bearingTo(endLocation);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(routeFirstPoint)      // Sets the center of the map to Mountain View
                .zoom(19)                   // Sets the zoom
                .bearing(targetBearing)                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void updateCameraBering(GoogleMap googleMap, int currentPoint, LatLng latLng) {
        if (currentPoint + 1 >= route.getPListSize())
            return;

        LatLng routeFirstPoint = route.getPointsOfRoute().get(currentPoint).getLatLng();
        Location startLocation = new Location("startingPoint");
        startLocation.setLatitude(routeFirstPoint.latitude);
        startLocation.setLongitude(routeFirstPoint.longitude);

        Location endLocation = new Location("endingPoint");
        endLocation.setLatitude(latLng.latitude);
        endLocation.setLongitude(latLng.longitude);

        float targetBearing = endLocation.bearingTo(startLocation);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(routeFirstPoint)      // Sets the center of the map to Mountain View
                .zoom(googleMap.getCameraPosition().zoom)                   // Sets the zoom
                .bearing(targetBearing)                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void prepareCameraForRide(GoogleMap googleMap, Location currentLocation) {
        googleMap.setMinZoomPreference(10.0f);

        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng routeStart = route.getPointsOfRoute().get(0).getLatLng();
        LatLng routeSecondPoint = route.getPointsOfRoute().get(1).getLatLng();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(routeStart)      // Sets the center of the map to Mountain View
                .zoom(19)                   // Sets the zoom
                .bearing((float) MapCalculator.calculateBearing(routeStart, routeSecondPoint))                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public boolean isLocationOnRoute(LatLng location, double tolerance) {
        if (route == null || routeLine == null) {
            return false;
        }

        return PolyUtil.isLocationOnPath(location, routeLine, true, tolerance);
    }

    public PoiItem findClosestPoi(LatLng latLng) {
        int closestPoiId = 0;
        if (route == null) {
            return null;
        }

        double result = 0;
        double closestResult = 10000;
        for (PoiItem item : route.getPoiItemList()
                ) {

            result = MapCalculator.getDistanceFromPointToLnglat(item, latLng);
            if (result < closestResult) {
                closestResult = result;
                closestPoiId = route.getPoiItemList().indexOf(item);
            }

        }
        return route.getPoiItemList().get(closestPoiId);
    }

    public void checkIfNerbayPoi(Location location, Context context) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (isLocationOnRoute(latLng, 10)) {
            PoiItem poi = findClosestPoi(latLng);
            double distane = MapCalculator.getDistanceFromPointToLnglat(poi, latLng) * 100;

            Toast.makeText(context, poi.getPoi() + " dystans :" + distane + "m", Toast.LENGTH_SHORT).show();
            if (distane < 10) {
                poi.getPoi().playSound(context);
            }

        }

    }

    public void checkIfNerbayPoi(LatLng latLng, Context context) {

        if (isLocationOnRoute(latLng, 10)) {
            PoiItem poi = findClosestPoi(latLng);
            double distane = MapCalculator.getDistanceFromPointToLnglat(poi, latLng) * 100;

//            Toast.makeText(context, poi.getPoi() + " dystans :" + distane + "m", Toast.LENGTH_SHORT).show();
            if (distane < 10) {
                poi.getPoi().playSound(context);
            }

        }

    }

    public int findClosestPoint(LatLng latLng) {

        int closestPoiId = 0;
        if (route == null) {
            return -1;
        }

        double result = 0;
        double closestResult = 1000;
        for (Point item : route.getPointsOfRoute()
                ) {

            result = MapCalculator.getDistanceFromPointToLnglat(item, latLng);
            if (result < closestResult) {
                closestResult = result;
                closestPoiId = route.getPointsOfRoute().indexOf(item);
            }

        }

        return closestPoiId;
    }

    public int findClosestPoiIndex(LatLng latLng) {
        int closestPoiId = 0;
        if (route == null) {
            return 0;
        }

        double result = 0;
        double closestResult = 10000;
        for (PoiItem item : route.getPoiItemList()
                ) {

            result = MapCalculator.getDistanceFromPointToLnglat(item, latLng);
            if (result < closestResult) {
                closestResult = result;
                closestPoiId = route.getPoiItemList().indexOf(item);
            }

        }
        return closestPoiId;
    }

    public void showInfoAndPlaySound(LatLng postionLatLng, GoogleMap googleMap, View view) {
        if (isLocationOnRoute(postionLatLng, 10.0)) {
            int closestPoint = findClosestPoint(postionLatLng);
            updateCameraBering(googleMap, closestPoint +1 , postionLatLng);

            int closestPoi = findClosestPoiIndex(postionLatLng);
            PoiItem poiItem = route.getPoiItemList().get(closestPoi);
            double distane = MapCalculator.getDistanceFromPointToLnglat(poiItem, postionLatLng) * 100;
            checkIfNerbayPoi(postionLatLng,view.getContext());
            Snackbar.make(view,"Najbliższy POI "+ poiItem.getPoi() +" za " + distane +" m ",Snackbar.LENGTH_SHORT ).show();
        }
    }
}
