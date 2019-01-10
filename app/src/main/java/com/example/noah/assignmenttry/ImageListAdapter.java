package com.example.noah.assignmenttry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        private final RecyclerView mRecyclerView;

        private ImageViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.recyclerview);
            imageFile = itemView.findViewById(R.id.image_rec);
            title = itemView.findViewById(R.id.title_display);
        }
    }

    private final LayoutInflater mInflater;
    private List<ImageData> mImages; // Cached copy of words
    // Store the mImages to be used when canceled the search view.
    private List<ImageData> mImagesBackup;

    ImageListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    /**
     * TODO Add Comment
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_image, parent, false);
        context = parent.getContext();
        return new ImageViewHolder(itemView);
    }


    /**
     * TODO Add Comment
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

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



        // Set the click listener to show the detail of image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Invoke the ImageDetailOverview to show the detail of the clicked image

                // Create the instance of ImageData of the clicked image
                ImageData current = mImages.get(position);

                // Create the instance of ImageDetailOverview
                ImageDetailOverview imageDetailOverview = new ImageDetailOverview();

                // Pass the detail of the clicked image
                imageDetailOverview.setImageDetail(current.getImagePath(),
                        current.getTitle(),
                        current.getDescription(),
                        current.getLongitude(),
                        current.getLatitide(),
                        current.getTime());

                // Invoke the CallBack at StartFragment
                // TODO Need to be make sure whether is correct
                if (mImageListAdapterListener != null) {
                    mImageListAdapterListener.onImageListAdapterClick(imageDetailOverview);
                }
            }
        });
    }



    /**
     * Add images to adapter
     *
     * Invoke at StartFragment
     *
     * @param images
     */
    void setImages(List<ImageData> images) {
        mImages = images;
        mImagesBackup = mImages;
        notifyDataSetChanged();
    }



    /**
     * Clear all images in adapter For search
     *
     * Invoke at StartFragment
     */
    void clearImages(){
        mImages = null;
        notifyDataSetChanged();
    }


    /**
     * Restore the previous images before click the search view
     */
    void restoreImages(){
        mImages = mImagesBackup;
        notifyDataSetChanged();
    }



    /**
     * getItemCount() is called many times, and when it is first called,
     * mWords has not been updated (means initially, it's null, and we can't return null).
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (mImages != null)
            return mImages.size();
        else return 0;
    }




    /**
     * The CallBack for "StartFragment"
     * TODO Need to decide the exact words
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
