package com.pwr.projekt.enduroracepilot.interfaces;

import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

/**
 * Created by Sebastian on 2017-04-22.
 */

public interface RouteDetalisCallback {
     void itemOnListCliked(Point point);

     void passTheRouteMapFragment(Route route);
}
