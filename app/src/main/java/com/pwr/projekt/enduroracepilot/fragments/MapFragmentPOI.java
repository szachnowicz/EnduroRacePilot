package com.pwr.projekt.enduroracepilot.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.interfaces.OnSelectedPOIListener;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragmentPOI extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private OnSelectedPOIListener poiClickListener;
    private String ROUTE_ID_REFERENCE_KEY;
    private ArrayList<Point> pointsList;
    private int currentPoint = 0;

    public MapFragmentPOI() {
        // Required empty public constructor
    }

    public String getROUTE_ID_REFERENCE_KEY() {
        return ROUTE_ID_REFERENCE_KEY;
    }

    public void setROUTE_ID_REFERENCE_KEY(String ROUTE_ID_REFERENCE_KEY) {
        this.ROUTE_ID_REFERENCE_KEY = ROUTE_ID_REFERENCE_KEY;
    }

    //https://developer.android.com/training/location/receive-location-updates.html
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.poiAddingMapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.poi_adding_fragment, container, false);

        initialiseAddPoiButton();

        return mView;
    }

    private void initialiseAddPoiButton() {

        Button button = (Button) mView.findViewById(R.id.addPOIFragmentButton);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        poiClickListener.buttonClick();

                    }
                }
        );

        Button buttonNext = (Button) mView.findViewById(R.id.buttonNextPOI);
        Button buttonPrevious = (Button) mView.findViewById(R.id.buttonPreviousPOI);

        buttonNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentPoint < pointsList.size()) {

                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pointsList.get(currentPoint).getLatLng()));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            currentPoint++;

                        }

                    }
                }
        );
        buttonPrevious.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (currentPoint >= 1) {

                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pointsList.get(currentPoint).getLatLng()));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            currentPoint--;

                        }

                    }
                }
        );

    }

    /*
    this is really smart solition to communicate between fragment and activity

     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {

                poiClickListener = (OnSelectedPOIListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                                                     + " must implement OnSelected");

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.123, -64.045)).title("some title").snippet("snipedd"));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);

            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
//         mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//         mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        pointsList = new ArrayList<>();
        getAllPointsFromDatabase(Point.TABEL_NAME, ROUTE_ID_REFERENCE_KEY);
    }

    private void getAllPointsFromDatabase(String child, final String key) {
        new Database().readDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
                Toast.makeText(getContext(), "on start", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot chlid : children
                        ) {
                    Point value = chlid.getValue(Point.class);

                    if (value != null && value.getRouteID().equals(key) && !pointsList.contains(value)) {
                        pointsList.add(value);

                    }

                }
                addAllMarkers();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    private void addAllMarkers() {

        PolylineOptions options = new PolylineOptions();

        for (Point point : pointsList
                ) {
            MarkerOptions marek = new MarkerOptions();
            marek.position(point.getLatLng());
            marek.title(point.getDiscription());
            //mGoogleMap.addMarker(marek);
            options.add(point.getLatLng());

            Toast.makeText(getContext(), "added points", Toast.LENGTH_SHORT).show();
        }

        options.width(5)
                .color(Color.RED);

        Polyline line = mGoogleMap.addPolyline(options);

    }

}
