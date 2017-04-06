package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.MapFragmentPOI;
import com.pwr.projekt.enduroracepilot.fragments.PoiPickerFragment;
import com.pwr.projekt.enduroracepilot.interfaces.OnSelectedPOIListener;

public class MapFragmentActivity extends AppCompatActivity implements OnSelectedPOIListener {

    private FragmentManager fragmentManager;
    private MapFragmentPOI mapFragmentPOI;
    private FragmentTransaction transaction;
    private LinearLayout poiMapFragmentLayout;
    private LinearLayout poiPickerLayout;

    private PoiPickerFragment poiPickerFragment;
    private int defaultMapFragmentHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        // fragment staff init
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        // the fragments init
        mapFragmentPOI = new MapFragmentPOI();
        poiPickerFragment = new PoiPickerFragment();

//        transaction.add(R.id.poiPickerFragment, poiPickerFragment);
        transaction.add(R.id.mapFragment, mapFragmentPOI);
        transaction.commit();
        poiMapFragmentLayout = (LinearLayout) findViewById(R.id.mapLayoutID);
        poiPickerLayout = (LinearLayout) findViewById(R.id.poiPickerLayoutID);
//        poiMapFragmentLayout.setVisibility(View.GONE);
        poiPickerLayout.setVisibility(View.GONE);

//        defaultMapFragmentHeight = poiMapFragmentLayout.getLayoutParams().height;
    }

    @Override
    public void buttonClick() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.LayoutParams paramsMapLayout = poiMapFragmentLayout.getLayoutParams();

        if (poiPickerLayout.getVisibility() == View.VISIBLE) {
            poiPickerLayout.setVisibility(View.GONE);

            paramsMapLayout.height = metrics.heightPixels - 200;
        }
        else {
            //paramsMapLayout.height = (defaultMapFragmentHeight);
            paramsMapLayout.height = (3 * metrics.heightPixels / 5 + 150);
            poiPickerLayout.setVisibility(View.VISIBLE);

        }

        poiMapFragmentLayout.setLayoutParams(paramsMapLayout);
//        ViewGroup.LayoutParams paramsPickerLayout = poiPickerLayout.getLayoutParams();
//        paramsPickerLayout.height = metrics.heightPixels - paramsMapLayout.height;
//        poiPickerLayout.setLayoutParams(paramsPickerLayout);
    }
}
