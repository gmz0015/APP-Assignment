package com.example.noah.assignmenttry;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.noah.assignmenttry.database.ImageData;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class GridFragment extends Fragment {

    /* Instance Field */
    private BaseViewModel mViewModel;
    private FragmentManager mfragManager;
    private ImageListAdapter myAdapter;
    private Activity mActivity;

    private final int LIST_STATUS = 0;
    private final int SEARCH_WORD_STATUS = 1;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the handle of parent fragment
        mfragManager = getFragmentManager();

        setHasOptionsMenu(true);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("GridFragment", "onActivityCreated()");

        // The number of items within a row
        int numberOfColumns = 3;

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        myAdapter = new ImageListAdapter(mActivity.getApplicationContext());


        RecyclerView myrecyclerView = mActivity.findViewById(R.id.recyclerview);
        myrecyclerView.setAdapter(myAdapter);
        myrecyclerView.setLayoutManager(new GridLayoutManager(mActivity.getApplicationContext(), numberOfColumns));


        mViewModel.setImageDataTrigger(LIST_STATUS);
        mViewModel.getImageDataLiveData().observe(this, dataList -> {
            // update UI with data from dataList
            myAdapter.setImages(dataList);
        });



        // Set FloatingActionButton for access to gallery
        FloatingActionButton fab_gallery = getActivity().findViewById(R.id.fab_gallery);

        // Assign the listener to fab_gallery
        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(GridFragment.this, 0); // Important
            }
        });



        // Set FloatingActionButton for access to gallery
        FloatingActionButton fab_camera = getActivity().findViewById(R.id.fab_camera);

        // Assign the listener to fab_camera
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(GridFragment.this, 0); // Important
            }
        });


        // The Callback for "ImageListAdapter" to invoke "ImageDetailOverview"
        // Short Click
        myAdapter.setOnImageShortClickListener(new ImageListAdapter.imageShortListener(){
            @Override
            public void onImageShortClick(ImageDetailOverview imageDetailOverview){
                FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
                fragmentTransaction.hide(getFragment());
                fragmentTransaction.addToBackStack("Start Fragment").add(R.id.baseContainer, imageDetailOverview, "Image Detail").commit();
            }
        });

        // The Callback for "ImageListAdapter" to invoke "ImageDetailOverview"
        // Long Click
        myAdapter.setOnImageLongClickListener(new ImageListAdapter.imageLongListener() {
            @Override
            public void onImageLongClick(ImageData imageData) {
                showLongClickDialog(imageData);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("GridFragment", "onStart()");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("GridFragment", "onResume()");
    }



    /**
     * Set toolbar
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("GridFragment", "onCreateOptionsMenu()");

        // Clear the previous menu
        menu.clear();

        // Inflate the menu with main_menu.xml
        inflater.inflate(R.menu.main_menu, menu);

        // Set the home icon is menu
        ActionBar actionbar = ((AppCompatActivity) mActivity).getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // Set the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, new MenuItemCompat.OnActionExpandListener() {
            // Define the listener
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.i("GridFragment", "Menu Collapse");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.i("GridFragment", "Menu Expand");

                // Clear the adapter
                mViewModel.setImageDataTrigger(SEARCH_WORD_STATUS);
                return true;  // Return true to expand action view
            }
        });


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.i("GridFragment", "OnFocusChange with View: " + v);
                    mViewModel.setImageDataTrigger(LIST_STATUS);
                    return;
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String queryWord = "";

            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryTextChange(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newWord) {

                // To avoid the double search of the same content
                if (newWord.equals(queryWord)) {
                    return true;
                }

                // Update the query word
                queryWord = newWord;

                if (queryWord.trim().equals("")) {
                    Log.i("GridFragment", "onQueryTextChange and Clear image");
                    // if nothing in searchView, clear the adapter.
                    mViewModel.setImageDataTrigger(SEARCH_WORD_STATUS);
                    return true;
                } else {
                    Log.i("GridFragment", "onQueryTextChange and set search image with " + queryWord);
                    mViewModel.setSearchTrigger(queryWord);
                }
                return true;
            }
        });
    }




    /**
     * Check to see which action the user selected.
     * If the method does not recognize the user's action, it invokes the superclass method
     *
     * @param item the handle of the selected menu item
     * @return
     */
//    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case android.R.id.home:
                DrawerLayout mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.

                //the superclass can expand the action view
                return super.onOptionsItemSelected(item);

        }
    }




    /**
     * Show dialog for long click
     *
     * Edit or Delete
     */
    private void showLongClickDialog(ImageData imageData){
        final String[] items = { "Edit", "Delete"};
        AlertDialog.Builder longClickDialog =
                new AlertDialog.Builder(mActivity);
        longClickDialog.setTitle("What do you want to do");
        longClickDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {

                    // User click the Edit
                    Toast.makeText(mActivity,
                            "Edit",
                            Toast.LENGTH_SHORT).show();
                }

                if (item == 1) {

                    mViewModel.delete(imageData);

                    // User click the Delete
                    Toast.makeText(mActivity,
                            "The image has been deleted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        longClickDialog.show();
    }


    /**
     *
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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


    /**
     * image_add_preview to the grid
     *
     * @param returnedPhotos
     */
    private void onPhotosReturned(List<File> returnedPhotos) {
        for (File file: returnedPhotos) {
            ImageAddFragment imageAddFragment = new ImageAddFragment();
            imageAddFragment.setImage(file.getAbsolutePath());
            FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
            fragmentTransaction.hide(getFragment());
            fragmentTransaction.addToBackStack("Start Fragment").add(R.id.baseContainer, imageAddFragment, "Add Image").commit();
        }
    }


    private Fragment getFragment() { return this;}
}
