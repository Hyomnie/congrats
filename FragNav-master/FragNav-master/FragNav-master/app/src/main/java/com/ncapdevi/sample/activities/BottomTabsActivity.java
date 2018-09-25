package com.ncapdevi.sample.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavSwitchController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.ncapdevi.fragnav.tabhistory.FragNavTabHistoryController;
import com.ncapdevi.sample.R;
import com.ncapdevi.sample.fragments.BaseFragment;
import com.ncapdevi.sample.fragments.FriendsFragment;
import com.ncapdevi.sample.fragments.GoogleFragment;
import com.ncapdevi.sample.fragments.NearbyFragment;
import com.ncapdevi.sample.fragments.RecentsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class BottomTabsActivity extends AppCompatActivity implements
        BaseFragment.FragmentNavigation,
        FragNavController.TransactionListener,
        FragNavController.RootFragmentListener {
    //Better convention to properly name the indices what they are in your app
    private final int INDEX_RECENTS = FragNavController.TAB1;
    private final int INDEX_FAVORITES = FragNavController.TAB2;
    private final int INDEX_NEARBY = FragNavController.TAB3;
    private final int INDEX_FRIENDS = FragNavController.TAB4;

    private FragNavController mNavController;
    final static int MY_PERMISSIONS_FINE_LOCATION = 99;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ncapdevi.sample.R.layout.activity_bottom_tabs);

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottomTabsActivity.this, startActivity.class);
                //액티비티 시작!
                startActivity(intent);

            }
        });
        requestPermissionGPS();
        final BottomBar bottomBar = findViewById(R.id.bottomBar);
        boolean initial = savedInstanceState == null;
        if (initial) {
            bottomBar.selectTabAtPosition(INDEX_RECENTS);
        }

        mNavController =
                FragNavController.newBuilder(savedInstanceState,
                        getSupportFragmentManager(), R.id.container)
                        .transactionListener(this)
                        .rootFragmentListener(this, 5)
                        .popStrategy(FragNavTabHistoryController.UNIQUE_TAB_HISTORY)
                        .switchController(new FragNavSwitchController() {
                            @Override
                            public void switchTab(int index,
                                                  FragNavTransactionOptions transactionOptions) {
                                bottomBar.selectTabAtPosition(index);
                            }
                        })
                        .build();

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bb_menu_recents:
                        mNavController.switchTab(INDEX_RECENTS);
                        break;
                    case R.id.bb_menu_favorites:
                        mNavController.switchTab(INDEX_FAVORITES);
                        break;
                    case R.id.bb_menu_friends:
                        mNavController.switchTab(INDEX_FRIENDS);
                        break;
                    case R.id.bb_menu_nearby:
                        mNavController.switchTab(INDEX_NEARBY);
                        break;
                }
            }
        }, initial);

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                mNavController.clearStack();
            }
        });
    }


    public void requestPermissionGPS() {
       if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED){
           if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
           {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
               MY_PERMISSIONS_FINE_LOCATION);
           }
           else
           {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                       MY_PERMISSIONS_FINE_LOCATION);
           }
       }
       else
       {

       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_FINE_LOCATION :
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{

                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.popFragment()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment,
                                      FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to
        //tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_RECENTS:
                return RecentsFragment.newInstance(0);
            case INDEX_FAVORITES:
                return FriendsFragment.newInstance(0);
            case INDEX_NEARBY:
                return GoogleFragment.newInstance(0);
            case INDEX_FRIENDS:
                return NearbyFragment.newInstance(0);

        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popFragment();
                break;
        }
        return true;
    }


}
