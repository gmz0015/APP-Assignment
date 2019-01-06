package com.example.noah.assignmenttry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ImageDetailPicPopup extends Fragment implements OnMapReadyCallback {

    /* Instance Field */
    private String title;
    private String description;
    private Double longitude;
    private Double latitude;
    private String time;

    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    /**
     * Default Constructor
     */
    public ImageDetailPicPopup() {}

    /**
     * Save parameters
     * @param title
     * @param description
     * @param longitude
     * @param latitude
     * @param time
     * @return
     */
    public static ImageDetailPicPopup newInstance(String title, String description, Double longitude, Double latitude, String time) {
        ImageDetailPicPopup imageDetailPicPopup = new ImageDetailPicPopup();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Description", description);
        bundle.putDouble("Longitude", longitude);
        bundle.putDouble("Latitude", latitude);
        bundle.putString("Time", time);
        imageDetailPicPopup.setArguments(bundle);

        return imageDetailPicPopup;
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

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MyDialogStyleBottom);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.image_detail_info, container, false);
//        inflater.inflate(R.layout.image_detail_info, container, false)

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView titleView = getActivity().findViewById(R.id.title_detail);
        TextView desView = getActivity().findViewById(R.id.description_detail);
        TextView timeView = getActivity().findViewById(R.id.time_detail);
        titleView.setText(title);
        desView.setText(description);
        timeView.setText(time);

//        setContentView(R.layout.activity_raw_map);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) getActivity().findViewById(R.id.mapView_detail);
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
