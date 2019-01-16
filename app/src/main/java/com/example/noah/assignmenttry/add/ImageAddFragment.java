package com.example.noah.assignmenttry.add;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noah.assignmenttry.ImageTabLayoutAdapter;
import com.example.noah.assignmenttry.R;
import com.example.noah.assignmenttry.basis.BaseViewModel;
import com.example.noah.assignmenttry.database.ImageData;

import java.util.ArrayList;

/**
 * The fragment for adding image
 *
 * This is an overview fragment. It contains ImageAddInfoFragment and ImageAddPreviewFragment
 */
public class ImageAddFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button saveButton;
    private EditText titleText;
    private EditText descriptionText;
    private EditText dateText;

    private BaseViewModel mViewModel;
    private Activity mActivity;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ImageTabLayoutAdapter mImageTabLayoutAdapter;
    private ImageAddPreviewFragment imageAddPreviewFragment = new ImageAddPreviewFragment();
    private ImageAddInfoFragment imageAddInfoFragment = new ImageAddInfoFragment();


    private String imagePath;
    public static final String FILE = "Image File";
    private final String[] titles = new String[]{"Preview", "Add Detail"};

    public ImageAddFragment() {
    }

    public boolean setImage(String imagePath){
        this.imagePath = imagePath;
        return true;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_add, container, false);

        // Set the handle of component
        mTabLayout = (TabLayout) view.findViewById(R.id.addTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.addViewPager);
        saveButton = view.findViewById(R.id.button_save);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        imageAddPreviewFragment.setPath(imagePath);
        fragments.add(imageAddPreviewFragment);
        mTabLayout.addTab(mTabLayout.newTab());

        fragments.add(imageAddInfoFragment);
        mTabLayout.addTab(mTabLayout.newTab());

        mTabLayout.setupWithViewPager(mViewPager,false);
        mImageTabLayoutAdapter = new ImageTabLayoutAdapter(fragments, getChildFragmentManager());
        mViewPager.setAdapter(mImageTabLayoutAdapter);

        for(int i=0;i<titles.length;i++){
            mTabLayout.getTabAt(i).setText(titles[i]);
        }


        // Set the click listener to save the image
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = mActivity.findViewById(R.id.searchTitleText);
                descriptionText = mActivity.findViewById(R.id.searchDescriptionText);
                dateText = mActivity.findViewById(R.id.searchDateText);

                if (TextUtils.isEmpty(descriptionText.getText())) {
                    Toast.makeText(
                            getContext(),
                            R.string.empty_description_not_saved,
                            Toast.LENGTH_LONG).show();
                    String title = titleText.getText().toString();
                    String description = "add a description!";
                    ImageData image = new ImageData(imagePath,
                            title,
                            description,
                            imageAddInfoFragment.getCurrentLongitude(),
                            imageAddInfoFragment.getCurrentLatitude(),
                            imageAddInfoFragment.getCurrentDate());

                    mViewModel.insert(image);
                    getFragmentManager().popBackStack(null, 0);
                }else if (TextUtils.isEmpty(dateText.getText())) {
                    Toast.makeText(
                            getContext(),
                            R.string.empty_date_not_saved,
                            Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(titleText.getText())) {

                    // Title is not input
                    String title;
                    int start=imagePath.lastIndexOf("/");
                    int end=imagePath.lastIndexOf(".");
                    if(start!=-1 && end!=-1){
                        title = imagePath.substring(start+1,end);
                    }else{
                        title = null;
                    }

                    String description = descriptionText.getText().toString();
                    ImageData image = new ImageData(imagePath,
                            title,
                            description,
                            imageAddInfoFragment.getCurrentLongitude(),
                            imageAddInfoFragment.getCurrentLatitude(),
                            imageAddInfoFragment.getCurrentDate());

                    mViewModel.insert(image);
                    getFragmentManager().popBackStack(null, 0);
                }else {
                    String title = titleText.getText().toString();
                    String description = descriptionText.getText().toString();
                    ImageData image = new ImageData(imagePath,
                            title,
                            description,
                            imageAddInfoFragment.getCurrentLongitude(),
                            imageAddInfoFragment.getCurrentLatitude(),
                            imageAddInfoFragment.getCurrentDate());

                    mViewModel.insert(image);
                    getFragmentManager().popBackStack(null, 0);
                }
            }
        });
        mViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("ImageAddFragment", "onCreateOptionsMenu()");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionbar = ((AppCompatActivity) mActivity).getSupportActionBar();

        // Enable the Up button
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        menu.clear();
        inflater.inflate(R.menu.child_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Log.i("ImageAddFragment", "onOptionItemSelected");
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
