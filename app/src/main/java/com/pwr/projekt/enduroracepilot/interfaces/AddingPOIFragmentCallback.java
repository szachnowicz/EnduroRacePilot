package com.pwr.projekt.enduroracepilot.interfaces;

import com.pwr.projekt.enduroracepilot.model.MapEntity.PoiItem;

/**
 * Created by Sebastian on 2017-04-04.
 */

public interface AddingPOIFragmentCallback {

    void showPoiPicker();

    void onPoiItemChoose(PoiItem poiItem);

}
