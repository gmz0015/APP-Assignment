package com.example.noah.assignmenttry.basis;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.noah.assignmenttry.detail.ImageDetailOverview;
import com.example.noah.assignmenttry.R;

/**
 * This the basic activity. Invoke the grid fragment, map fragment and search fragment
 */
public class BaseActivity extends AppCompatActivity implements ImageDetailOverview.OnImageDetailListener {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
    private DrawerLayout mDrawerLayout;

    private static BaseActivity activity;

    @Override
    protected void onResume(){
        super.onResume();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        Log.i("BaseActivity", "onResume()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        checkPermissions(getApplicationContext());

        // Create a new instance of MapsFragment and GridFragment
        MapsFragment mapsFragment = new MapsFragment();
        GridFragment gridFragment = new GridFragment();
        SearchFragment searchFragment = new SearchFragment();

        activity = this;

        // Set the toolbar as the app bar for the activity
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionbar = getSupportActionBar();

        // Enable the Up button
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Log.i("BaseActivity", "onNavigationItemSelected");

                        // set item as selected to persist highlight
                        switch (menuItem.getItemId()) {
                            case R.id.nav_grid_view:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.baseContainer, gridFragment.newInstance())
                                        .commitNow();
                                break;

                            case R.id.nav_map_view:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.baseContainer, mapsFragment.newInstance())
                                        .commitNow();
                                break;

                            case R.id.nav_search:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.baseContainer, searchFragment.newInstance())
                                        .commitNow();
                                break;
                            default:
//                                getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.container, GridFragment.newInstance())
//                                        .commitNow();
                        }
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.baseContainer, gridFragment.getInstance())
                    .commitNow();
        }
    }

    @Override
    public void onImageDetail(String path, String title, String description) {
        ImageDetailOverview imageDetailOverview = new ImageDetailOverview();
        Bundle args = new Bundle();
        args.putString("Path", path);
        args.putString("Title", title);
        args.putString("Description", description);
        imageDetailOverview.setArguments(args);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.baseContainer, imageDetailOverview).commit();

    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;
    }

    private void checkPermissions(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                }

            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Writing external storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }

    public static Activity getActivity() { return activity; }
}
