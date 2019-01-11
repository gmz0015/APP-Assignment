package com.example.noah.assignmenttry;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class ImageAddInfoFragment extends Fragment implements OnMapReadyCallback {

    /* Instance Field */
    private CalendarView calendarView;
    private TextView dateText;
    private TextView descriptionText;
    private FloatingActionButton fab_calendar;
    private MapView mMapView;

//    private static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    private Activity mActivity;
    private BaseViewModel mViewModel;
    private ChoseDateDialog mChoseDateDialog = new ChoseDateDialog();

    private String date;
    private Double longitude = null;
    private Double latitude = null;

    private static final int ACCESS_FINE_LOCATION = 123;
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    public ImageAddInfoFragment(){}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_add_info, container, false);

        // Set the handle of component
        calendarView = view.findViewById(R.id.calendarView);
        dateText = view.findViewById(R.id.searchDateText);
        descriptionText = view.findViewById(R.id.searchDescriptionText);
        fab_calendar = view.findViewById(R.id.fab_calendar);
        mMapView = view.findViewById(R.id.mapView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        fab_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoseDateDialog.show(getFragmentManager(), "Chose date");
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
//             Create location services client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Get the last known location
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
//                            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                            mMapView.getMapAsync(getFragment());

                        } else {
                            Log.i("MAP WRRONG", "Not get Current Location");
                        }
                    }
                });

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        mChoseDateDialog.setOnAddImageDate(new ChoseDateDialog.addImageDate() {
            @Override
            public void onDateChosen(int month, int day, int year) {
                date = month + "-" + day +"-" + year;
                dateText.setText(date);
            }
        });
    }

    public ImageAddInfoFragment getFragment() { return this; }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng appointLoc = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(appointLoc).title("New Image Position"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, 10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }



    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public Double getCurrentLongitude() { return this.longitude; }
    public Double getCurrentLatitude() { return this.latitude; }
    public String getCurrentDate() { return this.date; }
}
