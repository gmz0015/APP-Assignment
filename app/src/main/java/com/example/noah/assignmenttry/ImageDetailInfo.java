package com.example.noah.assignmenttry;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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

    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    public ImageDetailInfo(){}

    /**
     * Default Constructor
     */
    public ImageDetailInfo(String title, String description, Double longitude, Double latitude, String time) {
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MyDialogStyleBottom);
////         clone the inflater using the ContextThemeWrapper
//        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
//        View view = localInflater.inflate(R.layout.image_detail_info, null, false);

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

        TextView titleView = view.findViewById(R.id.title_detail);
        TextView desView = view.findViewById(R.id.description_detail);
        TextView timeView = view.findViewById(R.id.time_detail);
        mMapView = (MapView) view.findViewById(R.id.mapView_detail);
        titleView.setText(title);
        desView.setText(description);
        timeView.setText(time);

//        setContentView(R.layout.activity_raw_map);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view).setNegativeButton("Back", null);
        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng appointLoc = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(appointLoc).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(appointLoc));
        googleMap.setPadding(20,20,20,20); // Set unclickable
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
