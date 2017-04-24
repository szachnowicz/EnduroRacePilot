package com.pwr.projekt.enduroracepilot.MVP.repository.impl;

import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

public interface RouteRepisotryImpl  extends Repository<Route>
{

    void getAllRoutes(String userID, RouteView browseRouteView);
    void getSingleRoute(String routeID, RouteView editingRouteView);
}
