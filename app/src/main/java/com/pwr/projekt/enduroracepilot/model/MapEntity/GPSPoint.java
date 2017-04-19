package com.pwr.projekt.enduroracepilot.model.MapEntity;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sebastian on 2017-04-12.
 */

public class GPSPoint {

    private String routeID;
    private int pointID;
    private double lat;
    private double lng;

    public GPSPoint(String routeID, int pointID, MarkerOptions markerOptions) {
        this.routeID = routeID;
        this.pointID = pointID;
        this.lat = markerOptions.getPosition().latitude;
        this.lng = markerOptions.getPosition().longitude;

    }

    public GPSPoint(String routeID, int pointID, double lat, double lng) {
        this.routeID = routeID;
        this.pointID = pointID;
        this.lat = lat;
        this.lng = lng;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;

    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GPSPoint gpsPoint = (GPSPoint) o;

        if (pointID != gpsPoint.pointID) return false;
        if (Double.compare(gpsPoint.lat, lat) != 0) return false;
        if (Double.compare(gpsPoint.lng, lng) != 0) return false;
        return routeID != null ? routeID.equals(gpsPoint.routeID) : gpsPoint.routeID == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = routeID != null ? routeID.hashCode() : 0;
        result = 31 * result + pointID;
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
