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
import android.widget.ProgressBar;

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
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.OnSelectedPOIListener;
import com.pwr.projekt.enduroracepilot.model.MapEntity.PoiItem;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Point;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditingMapAndAddingPOIFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private MarkerOptions currentFocuseMarker;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private OnSelectedPOIListener poiClickListener;
    private String routeIDRefereceKey;
    private ArrayList<Point> pointsList;
    private int currentPoint = 0;
    private boolean zoom = false;
    private ProgressBar progressBar;

    public EditingMapAndAddingPOIFragment() {

    }

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

//        Toast.makeText(getActivity(), activity.getGetPointsList().size()+"sad", Toast.LENGTH_SHORT).show();
        initialiseAddPoiButton();
        initialiseProgresBar();

        return mView;
    }

    private void initialisecurrentFocuseMarker() {

        currentFocuseMarker = new MarkerOptions();
        currentFocuseMarker.draggable(true);

    }

    private void initialiseProgresBar() {
        progressBar = (ProgressBar) mView.findViewById(R.id.progressBar2);

    }

    private void initialiseAddPoiButton() {

        Button button = (Button) mView.findViewById(R.id.addPOIFragmentButton);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        poiClickListener.showPoiPicker();
                        if (currentFocuseMarker.getPosition() != null) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentFocuseMarker.getPosition()));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

//                            mGoogleMap.addMarker(currentFocuseMarker);
                        }
                    }
                }
        );

        Button buttonNext = (Button) mView.findViewById(R.id.buttonNextPOI);
        Button buttonPrevious = (Button) mView.findViewById(R.id.buttonPreviousPOI);

        buttonNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentPoint < pointsList.size() && progressBar.getVisibility() == View.GONE) {

                            LatLng latLng = new LatLng(pointsList.get(currentPoint).getLat(), pointsList.get(currentPoint).getLng());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                            currentFocuseMarker.position(latLng);

                            if (currentPoint < pointsList.size() - 1)
                                currentPoint++;

                        }

                    }
                }
        );
        buttonPrevious.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (currentPoint >= 1 && progressBar.getVisibility() == View.GONE) {

                            LatLng latLng = new LatLng(pointsList.get(currentPoint).getLat(), pointsList.get(currentPoint).getLng());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            currentFocuseMarker.position(latLng);

                            currentPoint--;

                        }

                    }
                }
        );

    }

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
        initialisecurrentFocuseMarker();
    }

    @Override
    public void onLocationChanged(Location location) {

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

    }

    public void setPointsList(ArrayList<Point> pointsList) {
        if (pointsList != null && pointsList.size() > 0) {
            this.pointsList = pointsList;
            addAllMarkers();
        }
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

    private void addAllMarkers() {

        PolylineOptions options = new PolylineOptions();

        for (Point point : pointsList
                ) {
            MarkerOptions marek = new MarkerOptions();
            marek.position(point.getLatLng());
            marek.title(point.getDiscription());
            mGoogleMap.addMarker(marek);
            options.add(point.getLatLng());

        }

        options.width(5).color(Color.RED);
        Polyline line = mGoogleMap.addPolyline(options);

        if (!pointsList.isEmpty()) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pointsList.get(0).getLatLng()));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

            currentFocuseMarker.position((pointsList.get(0).getLatLng()));
        }
        progressBar.setVisibility(View.GONE);

    }

    public void addPoiItemToMap(PoiItem poiItem) {
        currentFocuseMarker.icon(BitmapDescriptorFactory.fromResource(poiItem.getDrawableID()));
        currentFocuseMarker.title(poiItem.getPoiName());
        mGoogleMap.addMarker(currentFocuseMarker);

    }
}
