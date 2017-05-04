package com.pwr.projekt.enduroracepilot.model.MapEntity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.List;

/**
 * Created by Sebastian on 2017-04-22.
 */

public class MapCalculator {

    public static double getRouteLenghtInKm(Route route) {
        double result = 0;
        for (int i = 0; i < route.getPointsOfRoute().size() - 1; i++) {

            result += SphericalUtil.computeDistanceBetween(route.getPoint(i).getLatLng()
                    , route.getPoint(i + 1).getLatLng());
        }

        result = Math.floor(result) / 1000;
        return result;
    }

    public static double getDistanceFromPointToLnglat(Point point, LatLng latLng) {
        double result = 0;
        result = SphericalUtil.computeDistanceBetween(point.getLatLng(), latLng);
        result = Math.floor(result) / 100;
        return result;
    }

    public static double getDistaceFromPoint(List<Point> list, int pointIndex) {
        double distance = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            distance += SphericalUtil.computeDistanceBetween(list.get(i).getLatLng(), list.get(i + 1).getLatLng());
            if (i == pointIndex) {
                break;
            }
        }

        return Math.floor(distance);
    }

    public static double calculateBearing(LatLng pointA, LatLng pointB) {
        double longitude1 = pointA.longitude;
        double longitude2 = pointB.longitude;
        double latitude1 = Math.toRadians(pointA.longitude);
        double latitude2 = Math.toRadians(pointB.longitude);
        double longDiff = Math.toRadians(longitude2 - longitude1);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

}
