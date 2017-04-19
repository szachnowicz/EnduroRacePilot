package com.pwr.projekt.enduroracepilot.model.MapEntity;

import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<Point> pointsOfRoute;

    public Route(String routeID, Date date, String routeName, String routeDiscription, String author, ArrayList<Point> pointsOfRoute) {
        this.routeID = routeID;
        this.date = date;
        this.routeName = routeName;
        this.routeDiscription = routeDiscription;
        this.author = author;
        this.pointsOfRoute = pointsOfRoute;
    }

    public Route() {
    }

    public Route(String _routeID, Date date, String routeName, String routeDiscription, String author) {
        this.routeID = _routeID;
        this.date = date;
        this.routeName = routeName;
        this.routeDiscription = routeDiscription;
        this.author = author;
    }

    public String getRouteID() {

        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public ArrayList<Point> getPointsOfRoute() {
        return pointsOfRoute;
    }

    public void setPointsOfRoute(ArrayList<Point> pointsOfRoute) {
        this.pointsOfRoute = pointsOfRoute;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Nazwa trasy : ").append(routeName).append("\n");
        sb.append("Data utworzenia : " + date).append('\n');
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

    public String get_routeID() {

        return routeID;
    }

    public void set_routeID(String _routeID) {
        this.routeID = _routeID;
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
}

