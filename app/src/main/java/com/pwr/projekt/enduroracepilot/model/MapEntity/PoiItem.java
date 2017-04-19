package com.pwr.projekt.enduroracepilot.model.MapEntity;

import com.pwr.projekt.enduroracepilot.R;

import java.util.ArrayList;

/**
 * Created by Sebastian on 2017-04-05.
 */

public class PoiItem {

    private String poiName;
    private int drawableID;

    public PoiItem() {
    }

    public PoiItem(String poiName, int drawableID) {
        this.poiName = poiName;

        this.drawableID = drawableID;
    }

    public static ArrayList<PoiItem> createItemsToPicker() {
        ArrayList<PoiItem> mDateSet = new ArrayList<>();
// // TODO: 2017-04-06  utworzyć do kazdego String w @strings
        mDateSet.add(new PoiItem("Uskok (Drop)", R.drawable.drop));
        mDateSet.add(new PoiItem("schody", R.drawable.stairsd));
        mDateSet.add(new PoiItem("wąski nawrót w prawo ", R.drawable.turnleft));
        mDateSet.add(new PoiItem("wąski nawrót w lewo", R.mipmap.ic_launcher));
        mDateSet.add(new PoiItem("szarpa z lewej", R.mipmap.ic_launcher));
        mDateSet.add(new PoiItem("przeszkoda do przeskoczenia", R.mipmap.ic_launcher));
        mDateSet.add(new PoiItem("szuter enduro", R.mipmap.ic_launcher));
        mDateSet.add(new PoiItem("duże wystromienie", R.mipmap.ic_launcher));
        mDateSet.add(new PoiItem("szybko na odcinku XXX", R.mipmap.ic_launcher));

        return mDateSet;
    }

    public String getPoiName() {

        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

}


