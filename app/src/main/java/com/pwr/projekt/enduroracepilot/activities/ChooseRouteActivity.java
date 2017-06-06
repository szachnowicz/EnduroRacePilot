package com.pwr.projekt.enduroracepilot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.MVP.repository.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.RouteDetalisViewAdpater;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;
import com.pwr.projekt.enduroracepilot.model.SharedPref;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseRouteActivity extends AppCompatActivity {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";
    @BindView(R.id.progressBarRE)
    ProgressBar progressBar;
    @BindView(R.id.radioButtonMyRoute)
    RadioButton myRoute;
    @BindView(R.id.radioButtonSharedRoute)
    RadioButton sharedRoute;

    private ListView createdRouteListview;
    private ArrayList<Route> myRouteList;
    private ArrayAdapter<Route> adapter;
    private RouteDetalisViewAdpater adapterCustom;
    private ArrayList<Route> sharedList;
    private boolean ride = false;
    private SharedPref sharedPref;
    private ArrayList<Route> showList;
    private boolean isMyRoute = true;

    @Override
    protected void onStart() {
        super.onStart();
        sharedPref = new SharedPref(getApplicationContext());
        getAllResults(Route.TABEL_NAME);

    }

    private void getAllResults(String child) {
        new Database().readDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot snap : children
                        ) {

                    Route value = snap.getValue(Route.class);
                    if (!myRouteList.contains(value) && value.getAuthor().equals(sharedPref.getUserId())) {
                        myRouteList.add(value);
                    }
                    else {
                        if (sharedList.contains(value) && !value.getAuthor().equals(sharedPref.getUserId())) {
                            sharedList.add(value);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                progressBar.setVisibility(View.GONE);

                if (myRoute.isChecked()) {
                    adapterCustom.clear();
                    showList.clear();
                    showList.addAll(myRouteList);
                    adapterCustom.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    @OnClick(R.id.radioButtonMyRoute)
    public void onMyRouteClick(View view) {
        adapterCustom.clear();
        showList.clear();
        showList.addAll(myRouteList);
        adapterCustom.notifyDataSetChanged();

        if (myRoute.isChecked()) {
            sharedRoute.setChecked(false);
        }

    }

    @OnClick(R.id.radioButtonSharedRoute)
    public void sharedRouteClick(View view) {
        adapterCustom.clear();
        showList.clear();
        showList.addAll(sharedList);
        adapterCustom.notifyDataSetChanged();
        if (sharedRoute.isChecked()) {
            myRoute.setChecked(false);
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_route);

        Bundle extras = getIntent().getExtras();
        ButterKnife.bind(this);
        if (extras != null) {
            ride = extras.getBoolean("RIDE");

        }
        myRoute.setChecked(isMyRoute);
        sharedRoute.setChecked(!isMyRoute);

        myRouteList = new ArrayList<>();
        sharedList = new ArrayList<>();
        showList = new ArrayList<Route>();

        final Intent creatRoute = ride ? new Intent(this, RideActivity.class)
                : new Intent(this, AddingPoiElementsToRouteActivity.class);

        createdRouteListview = (ListView) findViewById(R.id.routeEditList);
        adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, showList);
        adapterCustom = new RouteDetalisViewAdpater(getApplicationContext(), showList);

        createdRouteListview.setAdapter(adapterCustom);

        createdRouteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                creatRoute.putExtra(ROUTE_ID, showList.get(postion).getRouteID());
                startActivity(creatRoute);

            }
        });
    }

}
