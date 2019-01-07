package com.example.noah.assignmenttry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDetailOverview extends Fragment{
    OnImageDetailListener mCallback;

    private String image_path;
    private String title;
    private String description;
    private Double longitude;
    private Double latitude;
    private String time;

    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    private Fragment currentFragment;

    public ImageDetailOverview() {}

    /**
     *
     * @param path
     * @param title
     * @param description
     * @param longitude
     * @param latitude
     * @param time
     * @return
     */
    public static ImageDetailOverview newInstance(String path, String title, String description, Double longitude, Double latitude, String time) {
        ImageDetailOverview imageDetailOverview = new ImageDetailOverview();
        Bundle bundle = new Bundle();
        bundle.putString("Path", path);
        bundle.putString("Title", title);
        bundle.putString("Description", description);
        bundle.putDouble("Longitude", longitude);
        bundle.putDouble("Latitude", latitude);
        bundle.putString("Time", time);
        imageDetailOverview.setArguments(bundle);
        return imageDetailOverview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = new Bundle();
        args = getArguments();

        this.image_path = args.getString("Path");
        this.title = args.getString("Title");
        this.description = args.getString("Description");
        this.longitude = args.getDouble("Longitude");
        this.latitude = args.getDouble("Latitude");
        this.time = args.getString("Time");
        return inflater.inflate(R.layout.image_detail_overview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.currentFragment = this;

        ImageView imageView = getActivity().findViewById(R.id.image_detail);
        Bitmap tempBitmap = BitmapFactory.decodeFile(image_path);
        imageView.setImageBitmap(tempBitmap);

        FloatingActionButton fab_info = getActivity().findViewById(R.id.fab_info);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageDetailInfo imageDetailInfo = ImageDetailInfo.newInstance(title, description, longitude, latitude, time);
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack("Image Detail Overview").add(R.id.info, imageDetailInfo, "Image Detail Info").commit();
                ImageDetailInfo editNameDialog = new ImageDetailInfo(title, description, longitude, latitude, time);
                editNameDialog.show(getFragmentManager(), "EditNameDialog");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnImageDetailListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnImageDetailListener ");
        }
    }

    // Container Activity must implement this interface
    public interface OnImageDetailListener {
        public void onImageDetail(String path, String title, String description);
    }
}
