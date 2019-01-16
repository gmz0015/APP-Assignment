package com.example.noah.assignmenttry;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noah.assignmenttry.database.ImageData;

import java.util.ArrayList;
import java.util.List;

public class ImageSearchResultFragment extends Fragment {

    /* Instance Field */
    private BaseViewModel mViewModel;
    private FragmentManager mfragManager;
    private ImageListAdapter myAdapter;
    private Activity mActivity;

    private String searchDate = null;
    private String searchTitle = null;
    private String searchDescription = null;
    private List<ImageData> imageDataList = new ArrayList<>();

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
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_search_result, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // The number of items within a row
        int numberOfColumns = 3;

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        myAdapter = new ImageListAdapter(mActivity.getApplicationContext());


        RecyclerView myrecyclerView = mActivity.findViewById(R.id.recyclerview);
        myrecyclerView.setAdapter(myAdapter);
        myrecyclerView.setLayoutManager(new GridLayoutManager(mActivity.getApplicationContext(), numberOfColumns));


        imageDataList = mViewModel.searchImageByWordAll(searchDate, searchTitle, searchDescription);
        myAdapter.setImages(imageDataList);

        // The Callback for "ImageListAdapter" to invoke "ImageDetailOverview"
        // Short Click
        myAdapter.setOnImageShortClickListener(new ImageListAdapter.imageShortListener(){
            @Override
            public void onImageShortClick(ImageDetailOverview imageDetailOverview){
                FragmentTransaction fragmentTransaction = mfragManager.beginTransaction();
                fragmentTransaction.hide(getParentFragment());
                fragmentTransaction.addToBackStack("Search Fragment").add(R.id.baseContainer, imageDetailOverview, "Image Detail").commit();
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



    public void setMfragManager(FragmentManager mfragManager) {
        this.mfragManager = mfragManager;
    }




    public void updateResult(String searchDate, String searchTitle, String searchDescription){
        imageDataList = mViewModel.searchImageByWordAll(searchDate, searchTitle, searchDescription);
        myAdapter.setImages(imageDataList);
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



    @Override
    public void onStart(){
        super.onStart();
        Log.i("ImageSearchResult", "onStart()");
    }



    @Override
    public void onPause(){
        super.onPause();
        Log.i("ImageSearchResult", "onPause()");
    }




    private Fragment getFragment() { return this;}
}
