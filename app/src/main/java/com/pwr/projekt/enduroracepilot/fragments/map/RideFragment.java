package com.pwr.projekt.enduroracepilot.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pwr.projekt.enduroracepilot.MVP.presenter.RidePresenter;
import com.pwr.projekt.enduroracepilot.R;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.PoiItem;
import com.pwr.projekt.enduroracepilot.model.MapEntity.entity.Route;
import com.pwr.projekt.enduroracepilot.model.SharedPref;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks
        , GoogleMap.OnMapClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 10;
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
    private LatLng postionLatLng;
    private boolean started = false;
    private SharedPref sharedPref;
    private long gpsInterval = 1000;

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
        sharedPref = new SharedPref(getContext());
        gpsInterval = sharedPref.getGpsRefresh();
        setSharePref();
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
        mLocationRequest.setInterval(gpsInterval);
        mLocationRequest.setFastestInterval(gpsInterval);
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
        postionLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!isZoom) {
            zoomAndInitCamera(location);
        }

        presenter.drawCircleOnMap(googleMap, postionLatLng);
        if (started && googleMap != null && view != null)
            presenter.showInfoAndPlaySound(postionLatLng, googleMap, view);

    }

    private void zoomAndInitCamera(Location location) {
        ;

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
        // googleMap.setOnMapClickListener(this);
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
        setUpPoiInfoWindow();
        setOnPoiInfoClickListener();
    }

    private void setOnPoiInfoClickListener() {

        if (googleMap != null) {

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    LatLng latLng = marker.getPosition();

                    for (PoiItem poiItem :
                            route.getPoiItemList()) {
                        if (poiItem.getLatLng().equals(latLng)) {
                            takeAndSavePoiPicture(marker, poiItem);
                            break;
                        }
                    }

                }
            });

        }

    }

    private void takeAndSavePoiPicture(Marker marker, PoiItem poiItem) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/EndruoRace/PoiPhoto/" + route.getRouteDiscription());
        if (!direct.exists()) {
            direct.mkdir();
        }

        File file = new File(direct, poiItem.getPointID() + "Poi.png");
        Uri imageUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        sharedPref.savePhotoPath(route, marker, file.getAbsolutePath());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    private void setUpPoiInfoWindow() {

        if (googleMap != null) {

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    if (marker.getTitle().equals(""))
                        return null;
                    View view = getLayoutInflater(getArguments()).inflate(R.layout.poi_marek_window, null);
                    view.setLayoutParams(new LinearLayout.LayoutParams(800, 400));
                    TextView textView = (TextView) view.findViewById(R.id.textViewPoiWindow);
                    ImageView imageView = (ImageView) view.findViewById(R.id.imageViewPoiWindow);
                    textView.setText(marker.getTitle().toString());
                    LatLng latLng = marker.getPosition();
                    String pathToFile = null;
                    for (PoiItem poiItem :
                            route.getPoiItemList()) {
                        if (poiItem.getLatLng().equals(latLng)) {
                            pathToFile = sharedPref.getSavePhotoPath(route, marker);
                        }
                    }

                    if (pathToFile != null && new File(pathToFile).exists()) {

                        imageView.setImageBitmap(BitmapFactory.decodeFile(pathToFile));

                    }
                    return view;
                }
            });

        }

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
        setSharePref();
    }

    public void setSharePref() {
        if (sharedPref != null) {
            // Toast.makeText(getContext(), sharedPref.getDistaneToPath()+"  "+sharedPref.getDistaneToPoi(), Toast.LENGTH_SHORT).show();
            presenter.setDistanceToPath(sharedPref.getDistaneToPath());
            presenter.setPoiDetectionDistance(sharedPref.getDistaneToPoi());
            gpsInterval = sharedPref.getGpsRefresh();
            presenter.setShowCirclePath(sharedPref.getPathCircle());
            presenter.setShowCirclePoi(sharedPref.getPoiCircle());
            if (googleMap != null && postionLatLng != null) {
                presenter.drawCircleOnMap(googleMap, postionLatLng);
            }
        }
    }

    @OnClick(R.id.startRideButton)
    public void startRideButton(View view) {

        if (route == null || route.getPListSize() < 1 || route.getPoiItemList().size() < 1) {
            return;
        }

        if (currentPoint == 0 && currentLocation != null) {
            presenter.prepareCameraForRide(googleMap, currentLocation);
        }
        currentPoint++;
        presenter.updateCameraBering(googleMap, currentPoint);
        started = true;

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

}
