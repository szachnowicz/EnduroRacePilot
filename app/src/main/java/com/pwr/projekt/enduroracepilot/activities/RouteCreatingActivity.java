package com.pwr.projekt.enduroracepilot.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.adapters.SectionsPagerAdapter;
import com.pwr.projekt.enduroracepilot.interfaces.FullRouteDataCallback;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Route;

import java.util.ArrayList;

public class RouteCreatingActivity extends AppCompatActivity implements FullRouteDataCallback {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String ROUTE_ID_REFERENCE_KEY;
    private Route theRoute;
    private ArrayList<Point> pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_creating);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ROUTE_ID_REFERENCE_KEY = extras.getString(AddingSingelRouteActivity.ROUTE_ID);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), ROUTE_ID_REFERENCE_KEY);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        pointsList = new ArrayList<>();
        theRoute = new Route();

    }

    @Override
    public Route getRoute() {

//        Toast.makeText(RouteCreatingActivity.this, theRoute.toString()+"\n"+theRoute.getPointsOfRoute().size(), Toast.LENGTH_SHORT).show();
        return theRoute;
    }

    @Override
    public void saveRoute(Route route) {
        try {

            new Database().getRefereceToObject(Route.TABEL_NAME, route.getRouteID()).setValue(route);
        } catch (DatabaseException ex) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllResults(Route.TABEL_NAME);
        getAllPointsFromDatabase(Point.TABEL_NAME, ROUTE_ID_REFERENCE_KEY);
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
                    if (value.get_routeID().equals(ROUTE_ID_REFERENCE_KEY)) {
                        theRoute = value;
                    }

                }
                if (theRoute.get_routeID() == null) {
                    Toast.makeText(RouteCreatingActivity.this, "somethingIs wrong with DB", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    private void getAllPointsFromDatabase(String child, final String key) {
        new Database().readDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot chlid : children
                        ) {
                    Point value = chlid.getValue(Point.class);

                    if (value != null
                            && value.getRouteID() != null
                            && value.getRouteID().equals(key)
                            && !pointsList.contains(value)) {
                        pointsList.add(value);
                    }

                }

                if (pointsList.size() <= 0) {
                    Toast.makeText(RouteCreatingActivity.this, "somethingIs wrong with DB", Toast.LENGTH_SHORT).show();
                }
                else {
                    theRoute.setPointsOfRoute(pointsList);
                    Toast.makeText(RouteCreatingActivity.this, "points list added  size - " + pointsList.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_route_creating, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
