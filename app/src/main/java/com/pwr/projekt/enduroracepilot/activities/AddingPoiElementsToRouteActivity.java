package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pwr.projekt.enduroracepilot.MVP.presenter.AddPoiPresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.AddingPOItoRouteView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.EditingMapAndAddingPOIFragment;
import com.pwr.projekt.enduroracepilot.fragments.PoiPickerFragment;
import com.pwr.projekt.enduroracepilot.interfaces.AddingPOIFragmentCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.PoiItem;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddingPoiElementsToRouteActivity extends AppCompatActivity implements AddingPOIFragmentCallback, AddingPOItoRouteView {

    @BindView(R.id.mapLayoutID)
    LinearLayout poiMapFragmentLayout;
    @BindView(R.id.poiPickerLayoutID)
    LinearLayout poiPickerLayout;
    private Route route;
    private FragmentManager fragmentManager;
    private EditingMapAndAddingPOIFragment mapFragmentPOI;
    private FragmentTransaction transaction;
    private PoiPickerFragment poiPickerFragment;

    private String ROUTE_ID_REFERENCE_KEY;

    private AddPoiPresenter addPoiPresenter;
    private ViewGroup.LayoutParams paramsMapLayout;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        Bundle extras = getIntent().getExtras();
        ButterKnife.bind(this);
        if (extras != null) {
            ROUTE_ID_REFERENCE_KEY = extras.getString(AddingSingelRouteActivity.ROUTE_ID);
        }

        settingUpTheFragmentsProperties();
        addPoiPresenter = new AddPoiPresenter(this);
        addPoiPresenter.getRoute(ROUTE_ID_REFERENCE_KEY);

    }

    private void settingUpTheFragmentsProperties() {

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        mapFragmentPOI = new EditingMapAndAddingPOIFragment();
        poiPickerFragment = new PoiPickerFragment();

        transaction.add(R.id.frameLayoutForPoiMap, mapFragmentPOI);
        transaction.add(R.id.framgePickerLayout, poiPickerFragment);

        transaction.commit();

        poiPickerLayout.setVisibility(View.GONE);

        initFragmestHightAndVisibilty();
        route = new Route();

    }

    private void initFragmestHightAndVisibilty() {

        paramsMapLayout = poiMapFragmentLayout.getLayoutParams();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        poiMapFragmentLayout.setLayoutParams(paramsMapLayout);
        poiPickerLayout.setVisibility(View.GONE);

        paramsMapLayout.height = metrics.heightPixels - 200;
    }

//    @Override
//    public void onStart() {
////        Toast.makeText(this, "on start", Toast.LENGTH_SHORT).show();
//        super.onStart();
//
//        getAllPointsFromDatabase(Point.TABEL_NAME, ROUTE_ID_REFERENCE_KEY);
//
//    }
//
//    private void getAllPointsFromDatabase(String child, final String key) {
//        new Database().readDataOnce(child, new OnGetDataListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for (DataSnapshot chlid : children
//                        ) {
//                    Point value = chlid.getValue(Point.class);
//
//                    if (value != null && value.getRouteID().equals(key) && !pointsList.contains(value)) {
//                        pointsList.add(value);
//
//                    }
//
//                }
//
//                mapFragmentPOI.setPointsList(pointsList);
//            }
//
//            @Override
//            public void onFailed(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    @Override
    public void showPoiPicker() {

        paramsMapLayout = poiMapFragmentLayout.getLayoutParams();

        if (poiPickerLayout.getVisibility() == View.VISIBLE) {
            poiPickerLayout.setVisibility(View.GONE);

            paramsMapLayout.height = metrics.heightPixels - 200;
        }
        else {
            paramsMapLayout.height = (3 * metrics.heightPixels / 4);
            poiPickerLayout.setVisibility(View.VISIBLE);

        }

        poiMapFragmentLayout.setLayoutParams(paramsMapLayout);

    }

    @Override
    public void onPoiItemChoose(PoiItem poiItem) {

        mapFragmentPOI.addPoiItemToMap(poiItem);

    }

    @Override
    public void displayRouteDetalis(List<Route> list) {

        mapFragmentPOI.setRouteFromActivityLevel(list.get(0),addPoiPresenter);
    }
}




