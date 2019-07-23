package com.tapin.tapin.activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tapin.tapin.R;
import com.tapin.tapin.fragment.CardDetailFragment;
import com.tapin.tapin.fragment.HomeFragment;
import com.tapin.tapin.fragment.MenuFoodListFragment;
import com.tapin.tapin.fragment.NotificationsFragment;
import com.tapin.tapin.fragment.OrderFragment;
import com.tapin.tapin.fragment.OrderSummaryFragment;
import com.tapin.tapin.fragment.PointsFragment;
import com.tapin.tapin.fragment.ProfileFragment;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.BottomNavigationViewHelper;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.Utils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    public static final String CURRENT_TAG = HomeFragment.class.getSimpleName();

    FrameLayout frame_home;
    FrameLayout frame_profile;
    FrameLayout frame_notifications;
    FrameLayout frame_points;

    LinearLayout llHome;
    ImageView ivHome;
    TextView tvHome;
    LinearLayout llProfile;
    ImageView ivProfile;
    TextView tvProfile;
    LinearLayout llNotifications;
    ImageView ivNotifications;
    TextView tvNotifications;
    LinearLayout llPoints;
    ImageView ivPoints;
    TextView tvPoints;

    boolean isHome;
    boolean isProfile;
    boolean isNotifications;
    boolean isPoints;

    private static final String SELECTED_ITEM = "SELECTED_ITEM";

    private int selectedItem;
    MenuItem selectedMenuItem;

    AlertMessages messages;

    public static final int PERMISSION_FINE_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        messages = new AlertMessages(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        initViews();

        llHome.performClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {

        frame_home = (FrameLayout) findViewById(R.id.frame_home);
        frame_profile = (FrameLayout) findViewById(R.id.frame_profile);
        frame_notifications = (FrameLayout) findViewById(R.id.frame_notifications);
        frame_points = (FrameLayout) findViewById(R.id.frame_points);

        llHome = (LinearLayout) findViewById(R.id.llHome);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        tvHome = (TextView) findViewById(R.id.tvHome);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        llNotifications = (LinearLayout) findViewById(R.id.llNotifications);
        ivNotifications = (ImageView) findViewById(R.id.ivNotifications);
        tvNotifications = (TextView) findViewById(R.id.tvNotifications);
        llPoints = (LinearLayout) findViewById(R.id.llPoints);
        ivPoints = (ImageView) findViewById(R.id.ivPoints);
        tvPoints = (TextView) findViewById(R.id.tvPoints);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (frame_home.getVisibility() == View.VISIBLE) {

                    messages.alert(HomeActivity.this, null, "Are you sure you want to cancel your current Order Process", "Yes", "No", null, new AlertMessages.AlertDialogCallback() {
                        @Override
                        public void clickedButtonText(String s) {

                            if (s.equalsIgnoreCase("Yes")) {

                                Constant.listOrdered = new ArrayList<OrderedInfo>();

                                FragmentManager fm = getSupportFragmentManager();
                                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }
                                setResult(RESULT_OK);
                                finish();

                            } else if (s.equalsIgnoreCase("No")) {

                            }

                        }
                    });

                }
                selectBottomMenu(frame_home, ivHome, R.drawable.tab_home_activated, tvHome);

                if (!isHome) {
                    isHome = true;
                    Fragment homeFragment = HomeFragment.newInstance("", "");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame_home, homeFragment);
                    ft.commit();
                }
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectBottomMenu(frame_profile, ivProfile, R.drawable.tab_profile_activated, tvProfile);

                if (!isProfile) {
                    isProfile = true;
                    Fragment profileFragment = ProfileFragment.newInstance("DASHBOARD", "");
//                    Fragment profileFragment = new CardDetailFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame_profile, profileFragment);
                    ft.commit();
                }

            }
        });

        llNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectBottomMenu(frame_notifications, ivNotifications, R.drawable.tab_notification_activated, tvNotifications);

                if (!isNotifications) {
                    isNotifications = true;
                    Fragment notificationsFragment = NotificationsFragment.newInstance("", "");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame_notifications, notificationsFragment);
                    ft.commit();
                }

            }
        });

        llPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectBottomMenu(frame_points, ivPoints, R.drawable.tab_points_activated, tvPoints);

                if (!isPoints) {
                    isPoints = true;
                    Fragment pointsFragment = PointsFragment.newInstance("", "");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame_points, pointsFragment);
                    ft.commit();
                }

            }
        });

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

        if (frame_home.getVisibility() == View.VISIBLE) {

            try {
                if (OrderFragment.isOrderChanged) {

                    MenuFoodListFragment.updateOrderData(Constant.listOrdered);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            super.onBackPressed();

        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            super.onBackPressed();
        }

//        Fragment fragment = getCurrentFragment();
//        Log.e("onBackPressed ", "fragment=" + fragment);

    }

    private void selectBottomMenu(FrameLayout selectedMenuFrame, ImageView iv, int imageId, TextView tv) {

        frame_home.setVisibility(View.GONE);
        frame_profile.setVisibility(View.GONE);
        frame_notifications.setVisibility(View.GONE);
        frame_points.setVisibility(View.GONE);

        selectedMenuFrame.setVisibility(View.VISIBLE);

        ivHome.setImageResource(R.drawable.tab_home_deactivated);
        ivProfile.setImageResource(R.drawable.tab_profile_deactivated);
        ivNotifications.setImageResource(R.drawable.tab_notification_deactivated);
        ivPoints.setImageResource(R.drawable.tab_points_deactivated);

        iv.setImageResource(imageId);

        tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray));
        tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray));
        tvNotifications.setTextColor(ContextCompat.getColor(this, R.color.gray));
        tvPoints.setTextColor(ContextCompat.getColor(this, R.color.gray));

        tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    public void addFragment(Fragment fragment, int frameId) {

        Log.e("ADD_FRAGMENT", "Fragment :-" + fragment.toString());

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(frameId, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
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