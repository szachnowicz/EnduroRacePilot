package com.pwr.projekt.enduroracepilot.MVP.view;

import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.List;

/**
 * Created by Sebastian on 2017-04-20.
 */

public interface BrowseRouteView extends  RouteView {
    void displayRouteDetalis(List<Route> routeList);


}
