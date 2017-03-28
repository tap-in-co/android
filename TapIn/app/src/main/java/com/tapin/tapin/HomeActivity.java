package com.tapin.tapin;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.tapin.tapin.fragment.HomeFragment;
import com.tapin.tapin.fragment.NotificationsFragment;
import com.tapin.tapin.fragment.PointsFragment;
import com.tapin.tapin.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

//
//        context = HomeActivity.this;
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        int permissionCheck = ContextCompat.checkSelfPermission(context,
//                android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
//            } else {
////                GPSTracker gps = new GPSTracker(this);
////                if (gps.canGetLocation()) {
////                    gps.getLatitude(); // returns latitude
////                    gps.getLongitude(); // returns longitude
////
////                    Log.w("Location", gps.getLatitude() + " " + gps.getLongitude());
////
////                }
//            }
//        } else {
//
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
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


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new NotificationsFragment(), "Notifications");
        adapter.addFragment(new PointsFragment(), "Points");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabTwo.setText("Profile");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabThree.setText("Notifications");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notifications, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabFour.setText("Points");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_points, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
