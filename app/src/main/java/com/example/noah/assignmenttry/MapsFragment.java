package com.example.noah.assignmenttry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.noah.assignmenttry.database.ImageData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends Fragment
        implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private BaseViewModel mViewModel;
    private ImageListAdapter myAdapter;
    private FragmentManager mfragManager;
    private static final int ACCESS_FINE_LOCATION = 123;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private static GoogleMap mMap;
    static Activity activity;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MapsFragment", "onCreate()");
        mfragManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("MapsFragment", "onCreateView()");
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MapsFragment", "onActivityCreated()");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            Log.i("(MapsFragment)", "mapFragment is not null");
        }else {
            Log.i("(MapsFragment)", "mapFragment is null");
        }

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        myAdapter = new ImageListAdapter(getActivity().getApplicationContext());

        mViewModel.getAllImages().observe(this, new Observer<List<ImageData>>() {
            @Override
            public void onChanged(@Nullable final List<ImageData> images) {
                // Update the cached copy of the words in the adapter.
                Log.i("MapsFragment", "Observer onChanged()");
//                myAdapter.setImages(images);
                for (ImageData imageData: images){
                    Double mLon = imageData.getLongitude();
                    Double mLat = imageData.getLatitide();
                    String mLastUpdateTime = imageData.getTime();
                    Log.i("MAP (ImageListAdapter)", "new location " + mLon.toString() + mLat.toString());
                    MapsFragment.getMap().addMarker(new MarkerOptions().position(new
                                LatLng(mLat, mLon))
                                .title(imageData.getTitle()));
                    MapsFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 10));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MapsFragment", "onResume()");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

//    private List<ImageData> imageDataList;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerClickListener(this);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 10));
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.

        // Check if a click count was set, then display the click count.
        String title = marker.getTitle();
        // The Callback for "ImageListAdapter" to invoke "ImageDetailOverview"
        ImageData current = mViewModel.getImageByTitle(title);
        ImageDetailOverview imageDetailOverview = ImageDetailOverview
                .newInstance(current.getImagePath(),
                        current.getTitle(),
                        current.getDescription(),
                        current.getLongitude(),
                        current.getLatitide(),
                        current.getTime());
        FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
        fragmentTransaction.hide(getFragment());
        fragmentTransaction.addToBackStack("Maps Fragment").add(R.id.container, imageDetailOverview, "Image Detail").commit();

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private Fragment getFragment() { return this;}

    public static void setActivity(Activity activity) {
        MapsFragment.activity = activity;
    }

    public static GoogleMap getMap() { return mMap; }

}
