package com.pwr.projekt.enduroracepilot.fragments.map;

import android.Manifest;
import android.app.Activity;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pwr.projekt.enduroracepilot.MVP.presenter.AddPoiPresenter;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.interfaces.AddingPOIFragmentCallback;
import com.pwr.projekt.enduroracepilot.model.MapEntity.Poi;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Point;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditingMapAndAddingPOIFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapClickListener {

    @BindView(R.id.progressBarPOI)
    ProgressBar progressBar;
    private Route route;
    private GoogleMap googleMap;
    private MapView mMapView;
    private View mView;
    private MarkerOptions currentFocuseMarker;
    private Marker currentMarker;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private AddingPOIFragmentCallback poiClickListener;
    private ArrayList<Point> pointsList;
    private int currentPoint = 0;

    private boolean zoom = false;
    private boolean mapIsReady = false;
    private AddPoiPresenter addPoiPresenter;

    public EditingMapAndAddingPOIFragment() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
        mView = inflater.inflate(R.layout.poi_adding_fragment, container, false);
        return mView;
    }

    private void initialisecurrentFocuseMarker() {

        currentFocuseMarker = new MarkerOptions();
        currentFocuseMarker.draggable(true);
        currentFocuseMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

    }

    @OnClick(R.id.addPOIFragmentButton)
    public void onClickAddButton(View view) {
        poiClickListener.showPoiPicker();
        if (currentFocuseMarker.getPosition() != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentFocuseMarker.getPosition()));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    @OnClick(R.id.buttonNextPOI)
    public void onClickNextButton(View view) {
        if (currentPoint < route.getPListSize()) {
            addPoiPresenter.changeFocuse(googleMap, route, ++currentPoint, currentFocuseMarker);
        }
    }

    @OnClick(R.id.buttonPreviousPOI)
    public void onClickPreviousButton(View view) {
        if (currentPoint > 0) {
            addPoiPresenter.changeFocuse(googleMap, route, --currentPoint, currentFocuseMarker);
        }
        else
            if (currentPoint == 0) {
                addPoiPresenter.changeFocuse(googleMap, route, 0, currentFocuseMarker);
            }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {

                poiClickListener = (AddingPOIFragmentCallback) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                                                     + " must implement OnSelected");

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
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
        initialisecurrentFocuseMarker();
        if (addPoiPresenter != null) {
            drawRouteOnMap();

        }
        mapIsReady = true;
    }



    public void drawRouteOnMap() {
        addPoiPresenter.drawRouteOnMap(googleMap, route);
        addPoiPresenter.addAllPoiIfExist(googleMap, route);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (!zoom && route != null && currentFocuseMarker != null) {
            addPoiPresenter.changeFocuse(googleMap, route, currentPoint, currentFocuseMarker);
            zoom = !zoom;
            currentFocuseMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            currentMarker = googleMap.addMarker(currentFocuseMarker);
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

    public void addPoiItemToMap(Poi poiItem) {
        addPoiPresenter.addPoiToMap(poiItem, googleMap, route, currentFocuseMarker);
    }

    public void setRouteFromActivityLevel(Route routeFromActivityLevel, AddPoiPresenter addPoiPresenter) {
        this.route = routeFromActivityLevel;
        this.addPoiPresenter = addPoiPresenter;
        progressBar.setVisibility(View.GONE);

        if (mapIsReady) {
            drawRouteOnMap();
        }
    }

    public void saveRoute() {
        addPoiPresenter.saveRoute(route);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        currentMarker.setPosition(latLng);
        currentFocuseMarker.position(latLng);

    }
}
