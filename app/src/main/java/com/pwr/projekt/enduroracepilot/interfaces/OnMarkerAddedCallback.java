package com.pwr.projekt.enduroracepilot.interfaces;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Sebastian on 2017-04-06.
 */

public interface OnMarkerAddedCallback {

    void passMarketList(ArrayList<MarkerOptions> markersList);

}
