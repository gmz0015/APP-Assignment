package com.example.noah.assignmenttry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDetailOverview extends Fragment{
    OnImageDetailListener mCallback;

    private String imagePath;
    private String title;
    private String description;
    private Double longitude;
    private Double latitude;
    private String time;

    private Activity mActivity;

    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyDhxfa-6SwMQ3bhnzxtWmG3UDOntkJhTcg";

    private Fragment currentFragment;

    public ImageDetailOverview() {}

    /**
     * Set the paremeters of image
     *
     * @param imagePath
     * @param title
     * @param description
     * @param longitude
     * @param latitude
     * @param time
     * @return
     */
    public boolean setImageDetail(String imagePath, String title, String description, Double longitude, Double latitude, String time){
        this.imagePath = imagePath;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
        try {
            mCallback = (OnImageDetailListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnImageDetailListener ");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("ImageDetailOverview", "onStart");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("ImageDetailOverview", "onCreateOptionsMenu()");
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
            Log.i("ImageDetailOverview", "onOptionItemSelected");
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_detail_overview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.currentFragment = this;

        ImageView imageView = mActivity.findViewById(R.id.image_detail);

        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//        Log.i("ImageDetailOverview", "imageView width is: " + targetW + " height is: " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile()
        BitmapFactory.decodeFile(imagePath);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
//        Log.i("ImageDetailOverview", "photo width is: " + photoW + " height is: " + photoH);


        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/384, photoH/525);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Matrix matrix = new Matrix();
        matrix.postScale(0.8f, 0.9f);//设置宽高放大比例（这里为等比例放大）
//        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                bitmap.getHeight(), matrix, true);//对现有bitmap进行放大
        imageView.setImageBitmap(bitmap);
//        Log.i("ImageDetailOverview", "original photo width is: " + bitmap.getWidth() + " height is: " + bitmap.getHeight());

//        int targetW = ;
//        int targetH = imageView.getHeight();
//        Log.i("ImageDetailOverview", "imageView width is: " + targetW + " height is: " + targetH);


        FloatingActionButton fab_info = mActivity.findViewById(R.id.fab_info);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailInfo imageDetailInfo = ImageDetailInfo.newInstance(title, description, longitude, latitude, time);
                imageDetailInfo.show(getFragmentManager(), "Image Detail Info Dialog");
            }
        });
    }

    // Container Activity must implement this interface
    public interface OnImageDetailListener {
        public void onImageDetail(String path, String title, String description);
    }
}
