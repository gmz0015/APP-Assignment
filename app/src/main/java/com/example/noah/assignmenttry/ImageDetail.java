package com.example.noah.assignmenttry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ImageDetail extends Fragment {
    OnImageDetailListener mCallback;

    private String image_path;
    private String title;
    private String description;
    private Double longitude;
    private Double latitude;
    private String time;

    public ImageDetail() {}

    /**
     *
     * @param imagePath
     * @return
     */
    public static ImageDetail newInstance(String path, String title, String description, Double longitude, Double latitude, String time) {
        ImageDetail imageDetail = new ImageDetail();
        Bundle bundle = new Bundle();
        bundle.putString("Path", path);
        bundle.putString("Title", title);
        bundle.putString("Description", description);
        bundle.putDouble("Longitude", longitude);
        bundle.putDouble("Latitude", latitude);
        bundle.putString("Time", time);
        imageDetail.setArguments(bundle);
        return imageDetail;
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
        return inflater.inflate(R.layout.image_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView imageView = getActivity().findViewById(R.id.image_detail);
        TextView titleView = getActivity().findViewById(R.id.title_detail);
        TextView desView = getActivity().findViewById(R.id.description_detail);
        TextView lonView = getActivity().findViewById(R.id.longitude_detail);
        TextView latView = getActivity().findViewById(R.id.latitude_deatil);
        TextView timeView = getActivity().findViewById(R.id.time_detail);

        Bitmap tempBitmap = BitmapFactory.decodeFile(image_path);
        imageView.setImageBitmap(tempBitmap);
        titleView.setText(title);
        desView.setText(description);
        lonView.setText(String.valueOf(longitude));
        latView.setText(String.valueOf(latitude));
        timeView.setText(time);

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
