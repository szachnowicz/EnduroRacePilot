package com.pwr.projekt.enduroracepilot.model.MapEntity.entity;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 2017-04-08.
 */
//@Entity

public class Route {

    public static final String TABEL_NAME = "ROUTE";

    private String routeID;
    private Date date;
    private String routeName;
    private String routeDiscription;
    private String author;
    private List<Point> pointsOfRoute;
    private List<PoiItem> poiItemList;

    public Route(String routeID, Date date, String routeName, String routeDiscription, String author, List<Point> pointsOfRoute) {
        this.routeID = routeID;
        this.date = date;
        this.routeName = routeName;
        this.routeDiscription = routeDiscription;
        this.author = author;
        this.pointsOfRoute = pointsOfRoute;
    }

    public Route(String routeID, Date date, String routeName, String routeDiscription, String author, List<Point> pointsOfRoute, List<PoiItem> poiItemList) {
        this.routeID = routeID;
        this.date = date;
        this.routeName = routeName;
        this.routeDiscription = routeDiscription;
        this.author = author;
        this.pointsOfRoute = pointsOfRoute;
        this.poiItemList = poiItemList;
    }

    public Route() {
        pointsOfRoute = new ArrayList<>();
        poiItemList = new ArrayList<>();

    }

    public Route(String _routeID, Date date, String routeName, String routeDiscription, String author) {
        this.routeID = _routeID;
        this.date = date;
        this.routeName = routeName;
        this.routeDiscription = routeDiscription;
        this.author = author;
    }

    public List<PoiItem> getPoiItemList() {
        return poiItemList;
    }

    public void setPoiItemList(List<PoiItem> poiItemList) {
        this.poiItemList = poiItemList;
    }

    public void addPoi(PoiItem poi) {
        if (poiItemList != null) {
            poiItemList.add(poi);
        }
    }

    public String getRouteID() {

        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public List<Point> getPointsOfRoute() {
        return pointsOfRoute;
    }

    public void setPointsOfRoute(List<Point> pointsOfRoute) {
        this.pointsOfRoute = pointsOfRoute;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sb.append("Nazwa trasy : ").append(routeName).append("\n");
        sb.append("Data utworzenia : " + formatter.format(date)).append('\n');
        sb.append("Autor : ").append(author).append('\n');
        sb.append("Opis trasy : ").append(routeDiscription).append('\n');

        return sb.toString();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDiscription() {
        return routeDiscription;
    }

    public void setRouteDiscription(String routeDiscription) {
        this.routeDiscription = routeDiscription;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (routeID != null ? !routeID.equals(route.routeID) : route.routeID != null) return false;
        if (date != null ? !date.equals(route.date) : route.date != null) return false;
        if (routeName != null ? !routeName.equals(route.routeName) : route.routeName != null)
            return false;
        if (routeDiscription != null ? !routeDiscription.equals(route.routeDiscription) : route.routeDiscription != null)
            return false;
        return author != null ? author.equals(route.author) : route.author == null;

    }

    @Override
    public int hashCode() {
        int result = routeID != null ? routeID.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (routeName != null ? routeName.hashCode() : 0);
        result = 31 * result + (routeDiscription != null ? routeDiscription.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    public Point getPoint(int index) {
        if (pointsOfRoute != null && index < pointsOfRoute.size() && index >= 0) {
            return pointsOfRoute.get(index);
        }
        return null;
    }

    public int getPListSize() {
        return pointsOfRoute.size();
    }

    public List<LatLng> onlyLatLngList() {
        List<LatLng> list = new ArrayList<>();
        for (Point p : pointsOfRoute
                ) {
            list.add(p.getLatLng());
        }

        return list;
    }
}

