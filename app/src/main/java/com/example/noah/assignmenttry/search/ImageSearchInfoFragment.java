package com.example.noah.assignmenttry.search;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.noah.assignmenttry.ChoseDateDialog;
import com.example.noah.assignmenttry.R;
import com.example.noah.assignmenttry.basis.BaseViewModel;

public class ImageSearchInfoFragment extends Fragment {

    /* Instance Field */
    private ChoseDateDialog mChoseDateDialog = new ChoseDateDialog();;
    private Activity mActivity;
    private BaseViewModel mViewModel;
    private TabLayout mTabLayout;
    private ImageSearchResultFragment mImageSearchResultFragment;

    private FloatingActionButton fab_calendar;
    private EditText searchDateView;
    private EditText searchTitleView;
    private EditText searchDescriptionView;
    private Button searchButton;

    private String searchDate = null;
    private String searchTitle = null;
    private String searchDescription = null;

    public ImageSearchInfoFragment(){}

    public static ImageSearchInfoFragment newInstance() {
        return new ImageSearchInfoFragment();
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
        View view = inflater.inflate(R.layout.image_search_info, container, false);

        // Set the handle of component
        fab_calendar = view.findViewById(R.id.fab_calendar_search);
        searchDateView = view.findViewById(R.id.searchDateText);
        searchTitleView = view.findViewById(R.id.searchTitleText);
        searchDescriptionView = view.findViewById(R.id.searchDateText);
        searchButton = view.findViewById(R.id.searchButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        fab_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoseDateDialog.show(getFragmentManager(), "Chose date");
            }
        });

        mChoseDateDialog.setOnAddImageDate(new ChoseDateDialog.addImageDate() {
            @Override
            public void onDateChosen(int month, int day, int year) {
                searchDate = month + "-" + day +"-" + year;
                searchDateView.setText(searchDate);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO swipe to next fragment
                searchDate = searchDateView.getText().toString();
                searchTitle = searchTitleView.getText().toString();
                searchDescription = searchDescriptionView.getText().toString();
                if(searchDate.equals("")){
                    searchDate = null;
                }
                if(searchTitle.equals("")){
                    searchTitle = null;
                }
                if(searchDescription.equals("")){
                    searchDescription = null;
                }
                mImageSearchResultFragment.updateResult(searchDate, searchTitle, searchDescription);
                mTabLayout.getTabAt(1).select();
            }
        });
    }

    public void setSearchTabLayout(TabLayout tabLayout){
        this.mTabLayout = tabLayout;
    }

    public void setResultFragment(ImageSearchResultFragment imageSearchResultFragment){
        this.mImageSearchResultFragment = imageSearchResultFragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("ImageSearchInfoFragment", "onResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i("ImageSearchInfoFragment", "onPause()");
    }

    @Override
    public void onDestroy(){
        Log.i("ImageSearchInfoFragment", "onDestroy()");
        super.onDestroy();
    }
}
