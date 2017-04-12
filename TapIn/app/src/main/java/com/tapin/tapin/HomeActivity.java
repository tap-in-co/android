package com.tapin.tapin;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapin.tapin.fragment.HomeFragment;
import com.tapin.tapin.fragment.NotificationsFragment;
import com.tapin.tapin.fragment.PointsFragment;
import com.tapin.tapin.fragment.ProfileFragment;
import com.tapin.tapin.utils.Debug;

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

        initHeader();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
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
    protected void onResume() {
        super.onResume();
        //initHeader();
    }

    public void initHeader() {
        ImageView ivHeaderLogo = (ImageView) findViewById(R.id.ivHeaderLogo);
        TextView tvHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        TextView tvHeaderLeft = (TextView) findViewById(R.id.tvHeaderLeft);
        TextView tvHeaderRight = (TextView) findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.VISIBLE);
        tvHeaderTitle.setVisibility(View.GONE);
        tvHeaderLeft.setVisibility(View.GONE);
        tvHeaderRight.setVisibility(View.GONE);


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


    ProfileFragment profileFragment;
    HomeFragment homeFragment;
    NotificationsFragment notificationsFragment;
    PointsFragment pointsFragment;

    private void setupViewPager(ViewPager viewPager) {
        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
        notificationsFragment = new NotificationsFragment();
        pointsFragment = new PointsFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "Home");
        adapter.addFragment(profileFragment, "Profile");
        adapter.addFragment(notificationsFragment, "Notifications");
        adapter.addFragment(pointsFragment, "Points");
        viewPager.setAdapter(adapter);

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_home, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabTwo.setText("Profile");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_profile, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabThree.setText("Notifications");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_notifications, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.view_tab_layout, null);
        tabFour.setText("Points");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_points, 0, 0);
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

    public void refreshPointsFragment()

    {
        pointsFragment.refreshData();
    }


}
