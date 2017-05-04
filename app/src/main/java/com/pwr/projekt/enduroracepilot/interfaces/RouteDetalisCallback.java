package com.pwr.projekt.enduroracepilot.interfaces;

import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

/**
 * Created by Sebastian on 2017-04-22.
 */

public interface RouteDetalisCallback {
     void itemOnListCliked(Point point);
     void passTheRouteMapFragment(Route route);
}
