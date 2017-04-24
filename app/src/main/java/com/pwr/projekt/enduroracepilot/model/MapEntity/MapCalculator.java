package com.pwr.projekt.enduroracepilot.model.MapEntity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

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
        return result;
    }

}
