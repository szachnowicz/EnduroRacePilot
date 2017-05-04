package com.pwr.projekt.enduroracepilot.MVP.presenter;

import com.pwr.projekt.enduroracepilot.MVP.repository.RouteRepository;
import com.pwr.projekt.enduroracepilot.MVP.view.RouteView;
import com.pwr.projekt.enduroracepilot.model.MapEntity.MapCalculator;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

/**
 * Created by Sebastian on 2017-04-20.
 */

public class BrowseRoutePresenter {

    private RouteRepository routeRepository;
    private RouteView browseRouteView;

    public BrowseRoutePresenter(RouteView browseRouteView) {
        this.browseRouteView = browseRouteView;
        routeRepository = new RouteRepository();
    }

    public void getAllData() {
        routeRepository.getAllRoutes("UserID", browseRouteView);
    }

    public void removeRoute(Route route) { routeRepository.remove(route); }

    public String prepareRouteDetalis(Route route) {
        StringBuilder sb = new StringBuilder();

        sb.append("Długosc trasy : ").append( (MapCalculator.getRouteLenghtInKm(route))+ "km \n" );
        sb.append("Liczba punktów tworząych trase " + route.getPointsOfRoute().size());

        return sb.toString();
    }

    public void saveRoute(Route route) {
        routeRepository.update(route);

    }

    public void removePointFromRoute(Route route, int position) {
        route.getPointsOfRoute().remove(position);

        for (int i = position; i < route.getPointsOfRoute().size(); i++) {
            route.getPoint(i).setPointID(i);
        }


    }
}
