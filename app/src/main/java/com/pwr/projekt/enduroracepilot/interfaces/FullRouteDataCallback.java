package com.pwr.projekt.enduroracepilot.interfaces;

import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

/**
 * Created by Sebastian on 2017-04-06.
 */

public interface FullRouteDataCallback {

    Route getRoute();

    void saveRoute(Route route);
}
