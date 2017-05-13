package com.tapin.tapin.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tapin.tapin.R;
import com.tapin.tapin.fragment.HomeFragment;
import com.tapin.tapin.fragment.NotificationsFragment;
import com.tapin.tapin.fragment.PointsFragment;
import com.tapin.tapin.fragment.ProfileFragment;
import com.tapin.tapin.utils.BottomNavigationViewHelper;
import com.tapin.tapin.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private Toolbar toolbar;

    BottomNavigationView bottomNavigationView;

    private static final String SELECTED_ITEM = "SELECTED_ITEM";

    private int selectedItem;
    MenuItem selectedMenuItem;

    public static final int PERMISSION_FINE_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("HomeActivity", "Refreshed token: " + refreshedToken);

        initBottomNavigationMenu();

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedMenuItem = bottomNavigationView.getMenu().findItem(selectedItem);
        } else {
            selectedMenuItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectFragment(selectedMenuItem);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initBottomNavigationMenu() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        selectFragment(item);

                        return true;
                    }
                });
    }

    private void selectFragment(MenuItem item) {
        Fragment selectedFragment = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.action_home:
                selectedFragment = HomeFragment.newInstance("", "");
                break;
            case R.id.action_profile:
                selectedFragment = ProfileFragment.newInstance("", "");
                break;
            case R.id.action_notifications:
                selectedFragment = NotificationsFragment.newInstance("", "");
                break;
            case R.id.action_points:
                selectedFragment = PointsFragment.newInstance("", "");
                break;
        }

        // update selected item
        selectedItem = item.getItemId();

        if (selectedFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, selectedFragment, selectedFragment.getTag());
            ft.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_FINE_LOCATION) {

            int permissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (Build.VERSION.SDK_INT >= 23) {
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                } else {

                }
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment fragment = getCurrentFragment();

        Debug.e("onBackPressed ", "fragment=" + fragment);

    }

    public void addFragment(Fragment fragment) {

        Debug.e("addFragment", "fragment=" + fragment.toString());
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.home_content, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(fragment.toString());
        trans.commit();
    }

    private Fragment getCurrentFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        int stackCount = fragmentManager.getBackStackEntryCount();
        if (fragmentManager.getFragments() != null)
            return fragmentManager.getFragments().get(stackCount > 0 ? stackCount - 1 : stackCount);
        else return null;
    }

}