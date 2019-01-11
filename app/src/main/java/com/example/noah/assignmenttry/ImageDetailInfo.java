package com.example.noah.assignmenttry;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ImageDetailInfo extends DialogFragment implements OnMapReadyCallback {

    /* Instance Field */
    private String title;
    private String description;
    private Double longitude;
    private Double latitude;
    private String time;

    private TextView titleView;
    private TextView desView;
    private TextView timeView;
    private MapView mMapView;


    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    /**
     * Default Constructor
     */
    public ImageDetailInfo(){}

    /**
     *
     * @param title
     * @param description
     * @param longitude
     * @param latitude
     * @param time
     * @return
     */
    public static ImageDetailInfo newInstance(String title, String description, Double longitude, Double latitude, String time) {
        ImageDetailInfo imageDeatilInfo = new ImageDetailInfo();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Description", description);
        bundle.putString("Time", time);
        imageDeatilInfo.setArguments(bundle);
        if (longitude != null){
            bundle.putDouble("Longitude", longitude);
            bundle.putDouble("Latitude", latitude);
        }else {
            bundle.putDouble("Longitude", 0);
            bundle.putDouble("Latitude", 0);
        }
        return imageDeatilInfo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = new Bundle();
        args = getArguments();

        this.title = args.getString("Title");
        this.description = args.getString("Description");
        this.longitude = args.getDouble("Longitude");
        this.latitude = args.getDouble("Latitude");
        this.time = args.getString("Time");

        View view = inflater.inflate(R.layout.image_detail_info, container);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.image_detail_info, null);

        // Set the handle of the title, description, time and mapVIew.
        titleView = view.findViewById(R.id.title_detail);
        desView = view.findViewById(R.id.description_detail);
        timeView = view.findViewById(R.id.time_detail);
        mMapView = (MapView) view.findViewById(R.id.mapView_detail);

        builder.setView(view).setNegativeButton("Back", null);
        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleView.setText(title);
        desView.setText(description);
        timeView.setText(time);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        if (latitude != 0) {
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng appointLoc = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(appointLoc).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(appointLoc));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
//        googleMap.setPadding(20,20,20,20); // Set unclickable
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
}
