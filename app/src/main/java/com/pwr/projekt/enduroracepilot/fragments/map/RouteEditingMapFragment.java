package com.pwr.projekt.enduroracepilot.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pwr.projekt.enduroracepilot.MVP.presenter.EditingRoutePresenter;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.MapDisplayingCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteEditingMapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapClickListener {

    @BindView(R.id.routeAddingMapView)
    MapView mMapView;
    private View fragmentView;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;

    private boolean zoomed = false;

    private Boolean trackGps = false;

    private ProgressBar progressBar;
    private EditingRoutePresenter editingRoutePresenter;

    ///***********************************************
    ///*************** Main ROUTE OBJECT ********************
    ///***********************************************
    private Route route;
    private boolean mapIsReady;
    private MapDisplayingCallback mapDisplayingCallback;
    private String ROUTE_ID_REFERENCE_KEY;

    public RouteEditingMapFragment() {

    }

    public static RouteEditingMapFragment newInstance(String _ROUTE_ID_REFERENCE_KEY) {

        RouteEditingMapFragment fragment = new RouteEditingMapFragment();
        fragment.setRouteRefernce(_ROUTE_ID_REFERENCE_KEY);
        return fragment;
    }

    @OnClick(R.id.trackGPSbutton)
    public void onClick(View v) {
        trackGps = !trackGps;
        Toast.makeText(getContext(), trackGps ? " Sledzenie GPS włączone " : " śledzenie wylączone ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_route_creation, container, false);
        ButterKnife.bind(this, fragmentView);
        route = new Route();
        editingRoutePresenter = new EditingRoutePresenter();

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        if (trackGps) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            editingRoutePresenter.addNewPointFromGpsToRoute(latLng, mGoogleMap, route);
            mapDisplayingCallback.passRouteToDetalisFragment(route);
        }

        if (!zoomed) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            zoomed = !zoomed;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapClickListener(this);
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.123, -64.045)).title("some title").snippet("snipedd"));
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

        editingRoutePresenter.contectThePoinsIntoRoute(mGoogleMap, route);
        mapIsReady = true;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (trackGps)
            return;

        editingRoutePresenter.onMapClick(latLng, mGoogleMap, route);
        mapDisplayingCallback.passRouteToDetalisFragment(route);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mapDisplayingCallback = (MapDisplayingCallback) activity;

            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                                                     + " must implement OnSelected");

            }
        }

    }

    public void zoomMapToPoint(Point point) {
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point.getLatLng()));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        }
    }

    public void passTheRoute(Route _route) {
        route = _route;
        if (mapIsReady)
            editingRoutePresenter.contectThePoinsIntoRoute(mGoogleMap, route);

    }

    private void setRouteRefernce(String _ROUTE_ID_REFERENCE_KEY) {
        this.ROUTE_ID_REFERENCE_KEY = _ROUTE_ID_REFERENCE_KEY;
    }
}
