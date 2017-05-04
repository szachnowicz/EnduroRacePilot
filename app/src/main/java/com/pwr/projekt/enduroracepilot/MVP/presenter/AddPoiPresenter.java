package com.pwr.projekt.enduroracepilot.MVP.presenter;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pwr.projekt.enduroracepilot.MVP.repository.RouteRepository;
import com.pwr.projekt.enduroracepilot.MVP.view.AddingPOItoRouteView;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.PoiItem;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

/**
 * Created by Sebastian on 2017-04-23.
 */

public class AddPoiPresenter {

    private RouteRepository routeRepository;
    private AddingPOItoRouteView browseRouteView;
    private boolean showPath = true;
    private boolean showPoins = true;
    private boolean showPOI = true;

    public AddPoiPresenter(AddingPOItoRouteView browseRouteView) {
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
            if (showPoins) {
                googleMap.addMarker(marek);
            }
            if (!options.getPoints().contains(point))
                options.add(point.getLatLng());
        }
        options.width(6).color(Color.RED);

        if (showPath) {
            Polyline line = googleMap.addPolyline(options);
        }
    }

    public void addAllPoiIfExist(GoogleMap googleMap, Route route) {
        for (PoiItem item : route.getPoiItemList()
                ) {
            MarkerOptions marker = new MarkerOptions();
            marker.draggable(true);
            marker.position(item.getLatLng());
            marker.title(item.getDiscription());
            marker.icon(BitmapDescriptorFactory.fromResource(item.getPoi().getDrawable()));
            if (showPOI) {
                googleMap.addMarker(marker);
            }
        }
        addOnPoiClickListener(googleMap);
    }

    private void addOnPoiClickListener(GoogleMap googleMap) {




    }

    public void changeFocuse(GoogleMap mGoogleMap, Route route, int currentPoint, MarkerOptions currentFocuseMarker) {

        if (currentPoint < 0 || currentPoint >= route.getPListSize())
            return;
        LatLng latLng = route.getPointsOfRoute().get(currentPoint).getLatLng();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        currentFocuseMarker.position(latLng);
    }

    public void addPoiToMap(Poi poi, GoogleMap mGoogleMap, Route route, MarkerOptions currentFocuseMarker) {

        PoiItem poiPoint = new PoiItem(route.getRouteID(), route.getPoiItemList().size(), currentFocuseMarker, poi);
        route.addPoi(poiPoint);
        currentFocuseMarker.icon(BitmapDescriptorFactory.fromResource(poi.getDrawable()));
        currentFocuseMarker.title(String.valueOf(poi.getDiscription()));
        mGoogleMap.addMarker(currentFocuseMarker);

    }

    public void saveRoute(Route route) {
        routeRepository.update(route);
    }

    public void showPath() {
        showPath = !showPath;
        browseRouteView.reDrawMap();
    }

    public void showPOI() {
        showPOI = !showPOI;
        browseRouteView.reDrawMap();
    }

    public void showPoints() {
        showPoins = !showPoins;
        browseRouteView.reDrawMap();
    }
}
