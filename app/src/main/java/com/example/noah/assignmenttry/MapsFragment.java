package com.example.noah.assignmenttry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    static Activity mActivity;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MapsFragment", "onCreate()");
        mfragManager = getFragmentManager();
        setHasOptionsMenu(true);
        checkPermission();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("MapsFragment", "onCreateView()");
        return inflater.inflate(R.layout.base_map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MapsFragment", "onActivityCreated()");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            Log.i("(MapsFragment)", "mapFragment is not null");
        } else {
            Log.i("(MapsFragment)", "mapFragment is null");
        }

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        mViewModel.getAllImages().observe(this, new Observer<List<ImageData>>() {
            @Override
            public void onChanged(@Nullable final List<ImageData> images) {
                // Update the cached copy of the words in the adapter.
                Log.i("MapsFragment", "Observer onChanged()");
//                myAdapter.setImages(images);
                for (ImageData imageData : images) {
                    if (imageData.getLongitude() != null) {
                        Double mLon = imageData.getLongitude();
                        Double mLat = imageData.getLatitide();
                        String mLastUpdateTime = imageData.getTime();
                        MapsFragment.getMap().addMarker(new MarkerOptions().position(new
                                LatLng(mLat, mLon))
                                .title(imageData.getTitle()));
                        MapsFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 14));
                    }
                }
            }
        });
    }

    /**
     * Set toolbar to have menu, search and setting options
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("MapsFragment", "onCreateOptionsMenu()");

        // Clear the previous menu
        menu.clear();

        // Inflate the menu with main_menu.xml
        inflater.inflate(R.menu.main_menu, menu);

        // Set the home icon is menu
        ActionBar actionbar = ((AppCompatActivity) mActivity).getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // Set the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            // Define the listener
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.i("GridFragment", "Menu Collapse");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.i("GridFragment", "Menu Expand");
                return true;  // Return true to expand action view
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                    // if no longer focus on the search view,
                    // then reset the zoom level.

                    // Reset the zoom level to 10
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String queryWord = "";

            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryTextChange(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newWord) {

                // To avoid the double search of the same content
                if (newWord.equals(queryWord)) {
                    return true;
                }

                // Update the query word
                queryWord = newWord;

                if (queryWord.trim().equals("")) {
                    Log.i("GridFragment", "onQueryTextChange and Clear image");

                    // if nothing in searchView, Reset the zoom level to 10.
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                    return true;
                } else {
                    Log.i("GridFragment", "onQueryTextChange and set search image with " + queryWord);

                    ImageData current = mViewModel.getImageByTitle(queryWord);
                    if (current != null) {

                        // if get the image by title
                        MapsFragment.getMap().moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(current.getLatitide(), current.getLongitude()), 8));
                    }
                }
                return true;
            }
        });
    }


    /**
     * Check to see which action the user selected.
     * If the method does not recognize the user's action, it invokes the superclass method
     *
     * @param item
     * @return
     */
//    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                AboutFragment aboutFragment = new AboutFragment();
                FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
                fragmentTransaction.hide(getFragment());
                fragmentTransaction.addToBackStack("Start Fragment").add(R.id.baseContainer, aboutFragment, "Add Image").commit();
                return true;

            case android.R.id.home:
                DrawerLayout mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MapsFragment", "onResume()");
    }

    public void checkPermission() {
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
    }


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        }
        mMap.setMyLocationEnabled(true);
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
        ImageDetailOverview imageDetailOverview = new ImageDetailOverview();
        imageDetailOverview.setImageDetail(current.getImagePath(),
                current.getTitle(),
                current.getDescription(),
                current.getLongitude(),
                current.getLatitide(),
                current.getTime());
        FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
        fragmentTransaction.hide(getFragment());
        fragmentTransaction.addToBackStack("Maps Fragment").add(R.id.baseContainer, imageDetailOverview, "Image Detail").commit();

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private Fragment getFragment() { return this;}

    public static GoogleMap getMap() { return mMap; }

}
