package com.example.noah.assignmenttry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

public class ImageAddPreviewFragment extends Fragment {
    private ImageView imageView;
    private String imagePath;
    private Activity mActivity;

    public ImageAddPreviewFragment(){}

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
        View view = inflater.inflate(R.layout.image_add_preview, container, false);
        imageView = view.findViewById(R.id.imagePreview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rotate = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        int degree = 0;
        switch (rotate){
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        Bitmap tempBitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap newBM = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix, false);

        imageView.setImageBitmap(newBM);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("ImageAddPreviewFragment", "onResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i("ImageAddPreviewFragment", "onPause()");
    }

    @Override
    public void onDestroy(){
        Log.i("ImageAddPreviewFragment", "onDestroy()");
        super.onDestroy();
    }

    public void setPath(String imagePath) { this.imagePath = imagePath; }
}
