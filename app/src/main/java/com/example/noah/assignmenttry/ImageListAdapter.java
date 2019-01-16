package com.example.noah.assignmenttry;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
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
        Log.i("imageListAdapter", "onBindViewHolder");
        ImageData current = mImages.get(position);

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Log.i("imageListAdapter", "asyncTask");
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    opts.inSampleSize = 4;
                    Bitmap tempBitmap = BitmapFactory.decodeFile(current.getImagePath(), opts);
                    ExifInterface exif = new ExifInterface(current.getImagePath());
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
                    opts.inJustDecodeBounds = false;
                    tempBitmap = BitmapFactory.decodeFile(current.getImagePath(), opts);
                    Bitmap newBM = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix, false);
                    return newBM;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object bitmap) {
                super.onPostExecute(bitmap);
                Log.i("imageListAdapter", "setImage");
                holder.imageFile.setImageBitmap((Bitmap) bitmap);
            }
        };

//        holder.imageFile.setTag(1,asyncTask);
//        asyncTask.execute();

        if (mImages != null) {
            holder.imageFile.setTag(R.id.ImageListAdapter,asyncTask);
            asyncTask.execute();
            holder.title.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.imageFile.setImageResource(R.drawable.example);
            holder.title.setText("No Title");
        }



        // Assign the long click listener on item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("ImageListAdapter", "OnLongClickListener");

                // Invoke the ImageDetailOverview to show the detail of the clicked image

                // Create the instance of ImageData of the clicked image
                ImageData current = mImages.get(position);

                // Invoke the CallBack at GridFragment
                // TODO Need to be make sure whether is correct
                if (mImageLongListener != null) {
                    mImageLongListener.onImageLongClick(current);
                }
                return true;
            }
        });



        // Set the click listener to show the detail of image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ImageListAdapter", "OnshortClickListener");

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

                // Invoke the CallBack at GridFragment
                // TODO Need to be make sure whether is correct
                if (mImageShortListener != null) {
                    mImageShortListener.onImageShortClick(imageDetailOverview);
                }
            }
        });
    }




    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        super.onViewRecycled(holder);
        AsyncTask asyncTask = (AsyncTask) holder.imageFile.getTag(R.id.ImageListAdapter);
        asyncTask.cancel(true);
    }



    /**
     * Add images to adapter
     *
     * Invoke at GridFragment
     *
     * @param image
     */
    void setImages(List<ImageData> image) {
        mImages = image;
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
     * The CallBack for "GridFragment"
     * TODO Need to decide the exact words
     */
    // CallBack
    public interface imageShortListener {
        void onImageShortClick(ImageDetailOverview imageDetailOverview);
    }

    private imageShortListener mImageShortListener;

    public void setOnImageShortClickListener(imageShortListener imageShortListener) {
        this.mImageShortListener = imageShortListener;
    }




    /**
     * The CallBack for "GridFragment"
     * TODO Need to decide the exact words
     */
    // CallBack
    public interface imageLongListener {
        void onImageLongClick(ImageData imageData);
    }

    private imageLongListener mImageLongListener;

    public void setOnImageLongClickListener(imageLongListener imageLongListener) {
        this.mImageLongListener = imageLongListener;
    }
}
