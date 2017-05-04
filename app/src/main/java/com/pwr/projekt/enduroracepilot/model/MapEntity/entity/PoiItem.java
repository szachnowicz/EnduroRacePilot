package com.pwr.projekt.enduroracepilot.model.MapEntity.entity;

import com.google.android.gms.maps.model.MarkerOptions;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;

/**
 * Created by Sebastian on 2017-04-05.
 */

public class PoiItem extends Point {

    public PoiItem() {
    }

    private Poi poi;

    public PoiItem(String routeID, int pointID, String discription, double lat, double lng, Poi poi) {
        super(routeID, pointID, discription, lat, lng);
        this.poi = poi;
    }

    public PoiItem(String routeID, int pointID, MarkerOptions markerOptions, Poi poi) {
        super(routeID, pointID, markerOptions);
        this.poi = poi;
    }

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }
}


