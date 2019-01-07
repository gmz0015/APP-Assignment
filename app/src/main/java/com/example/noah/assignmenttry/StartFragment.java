package com.example.noah.assignmenttry;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noah.assignmenttry.database.ImageData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.app.Activity.RESULT_OK;

public class StartFragment extends Fragment {

    private BaseViewModel mViewModel;
    private FragmentManager mfragManager;
    private ImageListAdapter myAdapter;

    static final int REQUEST_TAKE_PHOTO = 1;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mfragManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("StartFragment", "onActivityCreated()");

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        myAdapter = new ImageListAdapter(getActivity().getApplicationContext());

        int numberOfColumns = 4;
        RecyclerView myrecyclerView = getActivity().findViewById(R.id.recyclerview);
        myrecyclerView.setAdapter(myAdapter);
        myrecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), numberOfColumns));

        mViewModel.getAllImages().observe(this, new Observer<List<ImageData>>() {
            @Override
            public void onChanged(@Nullable final List<ImageData> images) {
                // Update the cached copy of the words in the adapter.
                Log.i("StartFragment", "Observer onChanged()");
                myAdapter.setImages(images);
            }
        });

        initEasyImage();

        FloatingActionButton fab_gallery = getActivity().findViewById(R.id.fab_gallery);
        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(StartFragment.this, 0); // Important
            }
        });

        FloatingActionButton fab_camera = getActivity().findViewById(R.id.fab_camera);
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EasyImage.openCamera(StartFragment.this, 0); // Important
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.i("StartFragment", "photoFile is: " + photoFile);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });

        // The Callback for "ImageListAdapter" to invoke "ImageDetailOverview"
        myAdapter.setOnImageListAdapterClickListener (new ImageListAdapter.imageListAdapterListener(){
            @Override
            public void onImageListAdapterClick(ImageDetailOverview imageDetailOverview){
                FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
                fragmentTransaction.hide(getFragment());
                fragmentTransaction.addToBackStack("Start Fragment").add(R.id.container, imageDetailOverview, "Image Detail").commit();
            }
        });
    }

    private void initEasyImage() {
        EasyImage.configuration(getContext())
                .setImagesFolderName("EasyImage sample")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Nothing need to do
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * add to the grid
     * @param returnedPhotos
     */
    private void onPhotosReturned(List<File> returnedPhotos) {
        for (File file: returnedPhotos) {
            AddImageFragment addImageFragment = AddImageFragment.newInstance(file.getAbsolutePath());
            FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
            fragmentTransaction.hide(getFragment());
            fragmentTransaction.addToBackStack("Start Fragment").add(R.id.container, addImageFragment, "Add Image").commit();
        }
    }

    private Fragment getFragment() { return this;}

}
