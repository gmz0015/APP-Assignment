package com.example.noah.assignmenttry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noah.assignmenttry.database.ImageData;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private Context context;

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageFile;
        private final TextView title;

        private ImageViewHolder(View itemView) {
            super(itemView);
            imageFile = itemView.findViewById(R.id.image_rec);
            title = itemView.findViewById(R.id.title_display);
        }
    }

    private final LayoutInflater mInflater;
    private List<ImageData> mImages; // Cached copy of words

    ImageListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_image, parent, false);
        context = parent.getContext();
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if (mImages != null) {
            ImageData current = mImages.get(position);
            Bitmap tempBitmap = BitmapFactory.decodeFile(current.getImagePath());
            holder.imageFile.setImageBitmap(tempBitmap);
            holder.title.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.imageFile.setImageResource(R.drawable.example);
            holder.title.setText("No Title");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageData current = mImages.get(position);
                ImageDetailOverview imageDetailOverview = ImageDetailOverview
                        .newInstance(current.getImagePath(),
                                current.getTitle(),
                                current.getDescription(),
                                current.getLongitude(),
                                current.getLatitide(),
                                current.getTime());
                if (mImageListAdapterListener != null) {
                    mImageListAdapterListener.onImageListAdapterClick(imageDetailOverview);
                }
            }
        });
    }

    void setImages(List<ImageData> images){
        mImages = images;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mImages != null)
            return mImages.size();
        else return 0;
    }

    /**
     * CallBack for "StartFragment"
     */
    private imageListAdapterListener mImageListAdapterListener;

    // CallBack
    public interface imageListAdapterListener {
        void onImageListAdapterClick(ImageDetailOverview imageDetailOverview);
    }

    public void setOnImageListAdapterClickListener (imageListAdapterListener  imageListAdapterListener) {
        this.mImageListAdapterListener = imageListAdapterListener;
    }
}
