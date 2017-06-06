package com.pwr.projekt.enduroracepilot.model.MapEntity.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Sebastian on 2017-04-08.
 */

public class Point {

    public static final String TABEL_NAME = "POINT";
    private String routeID;
    private int pointID;
    private String discription;
    private double lat;
    private double lng;

    public Point() {
    }

    public Point(String routeID, int pointID, String discription, double lat, double lng) {

        this.routeID = routeID;
        this.pointID = pointID;
        this.discription = discription;
        this.lat = lat;
        this.lng = lng;

    }

    public Point(String routeID, int pointID, MarkerOptions markerOptions) {
        this.routeID = routeID;
        this.pointID = pointID;
        this.lat = markerOptions.getPosition().latitude;
        this.lng = markerOptions.getPosition().longitude;
        //  this.discription = markerOptions.getTitle();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (pointID != point.pointID) return false;
        if (Double.compare(point.lat, lat) != 0) return false;
        if (Double.compare(point.lng, lng) != 0) return false;
        if (!routeID.equals(point.routeID)) return false;
        return discription != null ? discription.equals(point.discription) : point.discription == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = routeID.hashCode();
        result = 31 * result + pointID;
        result = 31 * result + (discription != null ? discription.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Point ");
        sb.append("numer Punktu " + pointID + "\n");
        sb.append("opis punktu " + discription + "\n");
        sb.append("długość geograficzna  " + lng + "\n");
        sb.append("szerokosc geograficzna " + lat + "\n");
        sb.append("\n");

        return sb.toString();
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
