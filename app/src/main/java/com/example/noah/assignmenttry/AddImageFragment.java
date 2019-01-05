package com.example.noah.assignmenttry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
    private BaseViewModel mViewModel;

    private static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationCallback mLocationCallback;

    private String image_path;
    public static final String PATH = "Image Path";
    private static final int ACCESS_FINE_LOCATION = 123;

    public AddImageFragment() {
    }

    /**
     *
     * @param imagePath
     * @return
     */
    public static AddImageFragment newInstance(String imagePath) {
        AddImageFragment addImageFragment = new AddImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, imagePath);
        addImageFragment.setArguments(bundle);
        return addImageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_image, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
            image_path = bundle.getString(PATH); // Image Path

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        // get the handle of imageView.
        imagePath_add = getActivity().findViewById(R.id.imageView_add);

        //get the handld of the title and description.
        title_input = getActivity().findViewById(R.id.title_input);
        description_input = getActivity().findViewById(R.id.description_input);

        Bitmap imageBitmap = BitmapFactory.decodeFile(image_path);
        imagePath_add.setImageBitmap(imageBitmap);

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

            return;
        }

//        startLocationUpdates();

        final Button button_get_location = getActivity().findViewById(R.id.button_get_location);
        button_get_location.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View view) {
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
                            EditText lon = getActivity().findViewById(R.id.new_lon);
                            TextView lat = getActivity().findViewById(R.id.new_lat);
                            TextView time = getActivity().findViewById(R.id.new_time);

                            lon.setText(String.valueOf(mCurrentLocation.getLongitude()));
                            lat.setText(String.valueOf(mCurrentLocation.getLatitude()));
                            time.setText(mLastUpdateTime);
                        }else {
                            Log.i("MAP WRRONG", "Not get Current Location");
                        }
                    }
                });

            }
        });

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
                    ImageData image = new ImageData(image_path,
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
