package com.example.noah.assignmenttry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
    private Activity mActivity;

    public AboutFragment() {
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

        View view = inflater.inflate(R.layout.about, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("AddFragment", "onCreateOptionsMenu()");

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
            Log.i("AboutFragment", "onOptionItemSelected");
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
