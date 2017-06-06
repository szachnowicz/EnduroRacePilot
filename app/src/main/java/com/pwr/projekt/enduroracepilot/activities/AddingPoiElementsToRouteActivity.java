package com.pwr.projekt.enduroracepilot.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pwr.projekt.enduroracepilot.MVP.presenter.AddPoiPresenter;
import com.pwr.projekt.enduroracepilot.MVP.view.AddingPOItoRouteView;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.fragments.PoiPickerFragment;
import com.pwr.projekt.enduroracepilot.fragments.map.EditingMapAndAddingPOIFragment;
import com.pwr.projekt.enduroracepilot.interfaces.AddingPOIFragmentCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddingPoiElementsToRouteActivity extends AppCompatActivity implements AddingPOIFragmentCallback, AddingPOItoRouteView {

    @BindView(R.id.mapLayoutID)
    LinearLayout poiMapFragmentLayout;
    @BindView(R.id.poiPickerLayoutID)
    LinearLayout poiPickerLayout;
    @BindView(R.id.toolbarPOI)
    Toolbar toolbar;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.showPath:
                Toast.makeText(this, "pokaż trase", Toast.LENGTH_SHORT)
                        .show();
                addPoiPresenter.showPath();
                break;
            // action with ID action_settings was selected
            case R.id.showPOI:
                Toast.makeText(this, "pokaz poi", Toast.LENGTH_SHORT)
                        .show();
                addPoiPresenter.showPOI();

                break;
            case R.id.showPoin:
                Toast.makeText(this, "pokaz point", Toast.LENGTH_SHORT)
                        .show();
                addPoiPresenter.showPoints();
                break;
            default:
                break;
        }

        return true;
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
    public void onPoiItemChoose(Poi poiItem) {

        mapFragmentPOI.addPoiItemToMap(poiItem);

    }

    @Override
    public void displayRouteDetalis(List<Route> list) {

        mapFragmentPOI.setRouteFromActivityLevel(list.get(0), addPoiPresenter);
    }

    @Override
    public void onBackPressed() {

        showAsForSaveRouteDialog();
    }

    private void showAsForSaveRouteDialog() {

        final Intent goToMainActivity = new Intent(this, MainActivity.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save route");
        alert.setMessage("Would you like to sava the route ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mapFragmentPOI.saveRoute();
                startActivity(goToMainActivity);
              //  finish();
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(goToMainActivity);
                //finish();
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public void reDrawMap() {
        mapFragmentPOI.drawRouteOnMap();
    }
}




