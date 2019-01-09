package com.example.noah.assignmenttry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noah.assignmenttry.database.ImageData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.Date;


public class AddImageFragment extends Fragment {

    private ImageView imagePath_add;
    private EditText title_input;
    private EditText description_input;
    private TextView longitude;
    private TextView latitude;
    private TextView time;
    private BaseViewModel mViewModel;

    private static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationCallback mLocationCallback;

    private Activity mActivity;

    private String imagePath;
    public static final String FILE = "Image File";
    private static final int ACCESS_FINE_LOCATION = 123;

    public AddImageFragment() {
    }

    public boolean setImage(String imagePath){
        this.imagePath = imagePath;
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_image, container, false);

        // Set th handle of component
        longitude= view.findViewById(R.id.new_lon);
        latitude = view.findViewById(R.id.new_lat);
        time = view.findViewById(R.id.new_time);
        imagePath_add = view.findViewById(R.id.imageView_add);
        title_input = view.findViewById(R.id.title_input);
        description_input = view.findViewById(R.id.description_input);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("AddImageFragment", "onCreateOptionsMenu()");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionbar = ((AppCompatActivity) mActivity).getSupportActionBar();

        // Enable the Up button
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Log.i("AddImageFragment", "onOptionItemSelected");
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        // Create location services client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Get the last known location
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mCurrentLocation = location;
                            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                            Log.i("MAP", "new location " + mCurrentLocation.toString() + "new location time " + mLastUpdateTime);
                            longitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
                            latitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
                            time.setText(mLastUpdateTime);
                        } else {
                            Log.i("MAP WRRONG", "Not get Current Location");
                        }
                    }
                });

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);


        // Get the dimensions of the View
        int targetW = imagePath_add.getWidth();
        int targetH = imagePath_add.getHeight();

        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imagePath);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
//        imagePath_add.setImageBitmap(bitmap);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imagePath_add.setImageBitmap(bitmap);


        final Button button = getActivity().findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View view) {
                if (TextUtils.isEmpty(title_input.getText()) || TextUtils.isEmpty(description_input.getText())) {
                    Toast.makeText(
                            getContext(),
                            R.string.empty_not_saved,
                            Toast.LENGTH_LONG).show();
                } else {
                    String title = title_input.getText().toString();
                    String description = description_input.getText().toString();
                    ImageData image = new ImageData(imagePath,
                            title,
                            description,
                            mCurrentLocation.getLongitude(),
                            mCurrentLocation.getLatitude(),
                            mLastUpdateTime);

                    mViewModel.insert(image);
                }
                getFragmentManager().popBackStack();
            }
        });
    }
}
