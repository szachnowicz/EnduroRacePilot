package com.pwr.projekt.enduroracepilot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.ArrayList;

public class EditingRouteActivity extends AppCompatActivity {

    public static final String ROUTE_ID = "ROUTE_REFERENCE";
    private ListView createdRouteListview;
    private ArrayList<Route> routeList;
    private ArrayAdapter<Route> adapter;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

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
                    if (!routeList.contains(value)) {
                        routeList.add(value);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_route);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRE);

        routeList = new ArrayList<>();
        final Intent creatRoute = new Intent(this, AddingPoiElementsToRouteActivity.class);
        createdRouteListview = (ListView) findViewById(R.id.routeEditList);
        adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);
        createdRouteListview.setAdapter(adapter);

        createdRouteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {

                creatRoute.putExtra(ROUTE_ID, routeList.get(postion).get_routeID());

                startActivity(creatRoute);

            }
        });
    }

}
