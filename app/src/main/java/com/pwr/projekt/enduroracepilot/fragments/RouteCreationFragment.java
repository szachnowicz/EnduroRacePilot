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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.activities.RouteCreatingActivity;
import com.pwr.projekt.enduroracepilot.interfaces.OnGetDataListener;
import com.pwr.projekt.enduroracepilot.interfaces.OnMarkerAddedCallback;
import com.pwr.projekt.enduroracepilot.model.Database;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteCreationFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapClickListener {

    private String ROUTE_ID;

    private View fragmentView;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private boolean zoomed = false;
    private int routeNO = 0;

    private OnMarkerAddedCallback onMarkerAddedCallback;

    private ArrayList<MarkerOptions> marekList;
    private ArrayList<Point> pointsList;
    /*****************************************************/
    private DatabaseReference pointsDatabaseReference;
    private String ROUTE_ID_REFERENCE_KEY;

    /*****************************************************/

    public RouteCreationFragment() {
        // Required empty public constructor
    }

    public static RouteCreationFragment newInstance(String _ROUTE_ID_REFERENCE_KEY) {

        RouteCreationFragment fragment = new RouteCreationFragment();
        // add some stuff if nesseery
        fragment.setRouteRefernce(_ROUTE_ID_REFERENCE_KEY);
        return fragment;
    }

    private void setRouteRefernce(String _ROUTE_ID_REFERENCE_KEY) {
        this.ROUTE_ID_REFERENCE_KEY = _ROUTE_ID_REFERENCE_KEY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_route_creation, container, false);

        pointsDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Point.TABEL_NAME);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) fragmentView.findViewById(R.id.routeAddingMapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        //move map camera

        if (!zoomed) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            zoomed = !zoomed;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        marekList = new ArrayList<>();
        pointsList = new ArrayList<>();
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapClickListener(this);
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.123, -64.045)).title("some title").snippet("snipedd"));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {

                onMarkerAddedCallback = (RouteCreatingActivity) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                                                     + " must implement OnSelected");

            }
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

        MarkerOptions marker = new MarkerOptions();

        marker.position(latLng);

        if (routeNO == 0) {
            marker.title("Start");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_flag));
        }
        else {
            marker.position(latLng).title("point nr" + routeNO).snippet("");
        }

        marker.draggable(true);
        mGoogleMap.addMarker(marker);
        routeNO++;

        ROUTE_ID = pointsDatabaseReference.push().getKey();
        Point point = new Point(ROUTE_ID_REFERENCE_KEY, routeNO, marker);
        pointsList.add(point);

        pointsDatabaseReference.child(ROUTE_ID).setValue(point);

        marekList.add(marker);
//        onMarkerAddedCallback.passMarketList(marekList);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        }
        routeNO = pointsList.size();

        options.width(5)
                .color(Color.RED);

        Polyline line = mGoogleMap.addPolyline(options);

    }

}
