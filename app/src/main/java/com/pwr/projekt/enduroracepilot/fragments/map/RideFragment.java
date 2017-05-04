package com.pwr.projekt.enduroracepilot.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pwr.projekt.enduroracepilot.MVP.presenter.RidePresenter;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks
        , GoogleMap.OnMapClickListener {

    @BindView(R.id.ridemapView)
    MapView rideMapView;
    private View view;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;
    private boolean mapIsReady = false;
    private boolean isZoom = false;
    private RidePresenter presenter;
    private Route route;
    private Location currentLocation;
    private int currentPoint = 0;
    // do wyjebania
    private MarkerOptions currentFocuseMarker;
    private Marker currentMarker;
    private LatLng postionLatLng;
    private boolean started = false;

    public RideFragment() {

    }

    public static RideFragment newInstance() {
        RideFragment fragment = new RideFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ride, container, false);
        ButterKnife.bind(this, view);

        if (rideMapView != null) {
            rideMapView.onCreate(null);
            rideMapView.onResume();
            rideMapView.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        postionLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!isZoom) {
            zoomAndInitCamera(location);

        }
        if (started)
            presenter.showInfoAndPlaySound(postionLatLng, googleMap, view);

    }

    private void zoomAndInitCamera(Location location) {
        // do wyjebania
        initialisecurrentFocuseMarker();
        currentFocuseMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        currentMarker = googleMap.addMarker(currentFocuseMarker);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        isZoom = !isZoom;
        currentLocation = location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMapClickListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                this.googleMap.setMyLocationEnabled(true);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }
        else {
            buildGoogleApiClient();
            this.googleMap.setMyLocationEnabled(true);
        }

        if (presenter != null && route != null) {
            drawRouteOnMap();
        }
        mapIsReady = true;

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void passRoute(Route route) {
        this.route = route;
        presenter.setRoute(route);
        if (mapIsReady) {
            drawRouteOnMap();
        }

    }

    public void drawRouteOnMap() {

        presenter.drawRouteOnMap(googleMap);
        presenter.addAllPoiIfExist(googleMap);

    }

    public void setPresenter(RidePresenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.startRideButton)
    public void startRideButton(View view) {
        if (currentPoint == 0) {
            presenter.prepareCameraForRide(googleMap, currentLocation);
        }
        currentPoint++;
        presenter.updateCameraBering(googleMap, currentPoint);
        started = true;

    }

    @Override
    public void onMapClick(LatLng latLng) {

//        currentMarker.setPosition(latLng);

//        presenter.checkIfNerbayPoi(latLng, getContext());
//        if (presenter.isLocationOnRoute(latLng, 10.0)) {
//            int closestPoint = presenter.findClosestPoint(latLng);
//
//            presenter.updateCameraBering(googleMap, closestPoint +1 , latLng);
//        }
//        presenter.updateCameraBering(googleMap, currentPoint, latLng);
    }

    private void initialisecurrentFocuseMarker() {

        currentFocuseMarker = new MarkerOptions();
        currentFocuseMarker.draggable(true);
        currentFocuseMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

    }
}
