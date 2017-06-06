package com.pwr.projekt.enduroracepilot.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.Marker;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Sebastian on 2017-05-18.
 */

public class SharedPref {

    private final String PREF = "EDRPref";
    private final String keyDistaneToPath = "DistaneToPath";
    private final String keyDistaneToPoi = "DistaneToPoi";
    private final String keyGpsRefresh = "gpsRefresh";
    private final String keyUserID = "userIDkey";
    private final String keyShowPathCircle = "pathCircle";
    private final String keyShowPoiCircle = "userPoiCircle";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putDistaneToPath(float distaneToPath) {

        editor.putFloat(keyDistaneToPath, distaneToPath);
        editor.commit();

    }

    public float getDistaneToPath() {
        return sharedPreferences.getFloat(keyDistaneToPath, 10);
    }

    public void putDistaneToPoi(float distaneToPoi) {

        editor.putFloat(keyDistaneToPoi, distaneToPoi);
        editor.commit();

    }

    public float getDistaneToPoi() {
        return sharedPreferences.getFloat(keyDistaneToPoi, 10);

    }

    public void putGpsRefresh(long gpsRefresh) {

        editor.putLong(keyGpsRefresh, gpsRefresh);
        editor.commit();

    }

    public long getGpsRefresh() {
        return sharedPreferences.getLong(keyGpsRefresh, 1000);

    }

    public String getUserId() {

        String userName = sharedPreferences.getString(keyUserID, "defaulUser");
        if (userName.equals("defaulUser")) {
            editor.putString(keyUserID, generateUserID());
            editor.commit();
        }
        return userName;
    }

    private String generateUserID() {

        SecureRandom random = new SecureRandom();

        return new BigInteger(130, random).toString(32);

    }

    public void putPoiCircle(Boolean circleVisibilty) {

        editor.putBoolean(keyShowPoiCircle, circleVisibilty);
        editor.commit();

    }

    public void putPathCircle(Boolean pathVisibilty) {

        editor.putBoolean(keyShowPathCircle, pathVisibilty);
        editor.commit();

    }

    public boolean getPathCircle() {
        return sharedPreferences.getBoolean(keyShowPathCircle, true);
    }

    public boolean getPoiCircle() {
        return sharedPreferences.getBoolean(keyShowPoiCircle, true);
    }

    public void savePhotoPath(Route route, Marker marker, String absolutePath) {

        editor.putString(route.getRouteID() + marker.getPosition().toString(), absolutePath);
        editor.commit();

    }

    public String getSavePhotoPath(Route route, Marker marker) {
        return sharedPreferences.getString(route.getRouteID() + marker.getPosition().toString(), "");

    }
}
