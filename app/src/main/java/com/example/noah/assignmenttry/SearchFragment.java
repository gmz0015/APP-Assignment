package com.example.noah.assignmenttry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Activity mActivity;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentManager mfragManager;
    private ImageTabLayoutAdapter mImageTabLayoutAdapter;
    private ImageSearchInfoFragment imageSearchInfoFragment = new ImageSearchInfoFragment();
    private ImageSearchResultFragment imageSearchResultFragment = new ImageSearchResultFragment();

    private final String[] titles = new String[]{"Info", "Result"};

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public SearchFragment getInstance() { return this; }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // get the handle of parent fragment
        mfragManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_search, container, false);

        // Set the handle of component
        mTabLayout = (TabLayout) view.findViewById(R.id.searchTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.searchViewPager);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        fragments.add(imageSearchInfoFragment);
        mTabLayout.addTab(mTabLayout.newTab());

        fragments.add(imageSearchResultFragment);
        mTabLayout.addTab(mTabLayout.newTab());

        mTabLayout.setupWithViewPager(mViewPager,false);
        mImageTabLayoutAdapter = new ImageTabLayoutAdapter(fragments, getChildFragmentManager());
        mViewPager.setAdapter(mImageTabLayoutAdapter);

        for(int i=0;i<titles.length;i++){
            mTabLayout.getTabAt(i).setText(titles[i]);
        }

        imageSearchInfoFragment.setSearchTabLayout(mTabLayout);
        imageSearchInfoFragment.setResultFragment(imageSearchResultFragment);
        imageSearchResultFragment.setMfragManager(mfragManager);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /**
     * Set toolbar
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("ImageSearchInfoFragment", "onCreateOptionsMenu()");

        // Clear the previous menu
        menu.clear();

        // Inflate the menu with main_menu.xml
        inflater.inflate(R.menu.child_menu, menu);

        // Set the home icon is menu
        ActionBar actionbar = ((AppCompatActivity) mActivity).getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
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
}
