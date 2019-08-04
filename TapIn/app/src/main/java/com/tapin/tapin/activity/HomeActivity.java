package com.tapin.tapin.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tapin.tapin.R;
import com.tapin.tapin.fragment.HomeFragment;
import com.tapin.tapin.fragment.MenuFoodListFragment;
import com.tapin.tapin.fragment.NotificationsFragment;
import com.tapin.tapin.fragment.OrderFragment;
import com.tapin.tapin.fragment.OrderSummaryFragment;
import com.tapin.tapin.fragment.PaymentFragment;
import com.tapin.tapin.fragment.PointsFragment;
import com.tapin.tapin.fragment.ProfileFragment;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    public static final String CURRENT_TAG = HomeFragment.class.getSimpleName();
    public static final int PERMISSION_FINE_LOCATION = 1001;
    private static final String SELECTED_ITEM = "SELECTED_ITEM";


    private static final List<String> FRAGMENT_NAMES_TO_GO_HOME = new ArrayList<>();

    FrameLayout frame_home;
    LinearLayout llHome;
    ImageView ivHome;
    TextView tvHome;


    FrameLayout frame_profile;
    LinearLayout llProfile;
    ImageView ivProfile;
    TextView tvProfile;

    FrameLayout frame_notifications;
    LinearLayout llNotifications;
    ImageView ivNotifications;
    TextView tvNotifications;

    FrameLayout frame_points;
    LinearLayout llPoints;
    ImageView ivPoints;
    TextView tvPoints;

    boolean isHome;
    boolean isProfile;
    boolean isNotifications;
    boolean isPoints;
    MenuItem selectedMenuItem;

    AlertMessages messages;
    private int selectedItem;

    static {
        FRAGMENT_NAMES_TO_GO_HOME.add(MenuFoodListFragment.class.getName());
        FRAGMENT_NAMES_TO_GO_HOME.add(OrderFragment.class.getName());
        FRAGMENT_NAMES_TO_GO_HOME.add(OrderSummaryFragment.class.getName());
        FRAGMENT_NAMES_TO_GO_HOME.add(PaymentFragment.class.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        messages = new AlertMessages(this);

        initViews();

        handleIntent();
    }

    private void handleIntent() {
        final Bundle args = getIntent().getExtras();

        if (args != null && args.getString("to_screen") != null) {
            if (args.getString("to_screen").equalsIgnoreCase("Profile")) {
                markProfileAsSelected();
            } else if (args.getString("to_screen").equalsIgnoreCase("Notification")) {
                markNotificationAsSelected();
            } else if (args.getString("to_screen").equalsIgnoreCase("Points")) {
                markPointsAsSelected();
            } else {
                llHome.performClick();
            }
        } else {
            llHome.performClick();
        }
    }

    private void initViews() {

        frame_home = findViewById(R.id.frame_home);
        frame_profile = findViewById(R.id.frame_profile);
        frame_notifications = findViewById(R.id.frame_notifications);
        frame_points = findViewById(R.id.frame_points);

        llHome = findViewById(R.id.llHome);
        ivHome = findViewById(R.id.ivHome);
        tvHome = findViewById(R.id.tvHome);
        llProfile = findViewById(R.id.llProfile);
        ivProfile = findViewById(R.id.ivProfile);
        tvProfile = findViewById(R.id.tvProfile);
        llNotifications = findViewById(R.id.llNotifications);
        ivNotifications = findViewById(R.id.ivNotifications);
        tvNotifications = findViewById(R.id.tvNotifications);
        llPoints = findViewById(R.id.llPoints);
        ivPoints = findViewById(R.id.ivPoints);
        tvPoints = findViewById(R.id.tvPoints);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowCancelOrder()) {
                    showClearOrderDialog();
                }
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markProfileAsSelected();
            }
        });

        llNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNotificationAsSelected();
            }
        });

        llPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markPointsAsSelected();
            }
        });

        if (BaseActivity.Companion.isPickUpOrder()) {
            markProfileAsSelected();
        } else {
            markHomeAsSelected();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_FINE_LOCATION) {

            int permissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
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

    private void markHomeAsSelected() {
        selectBottomMenu(frame_home, ivHome, R.drawable.tab_home_activated, tvHome);

        if (!isHome) {
            isHome = true;
            Fragment homeFragment = HomeFragment.newInstance("", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_home, homeFragment);
            ft.commit();
        }
    }

    private void markProfileAsSelected() {
        selectBottomMenu(frame_profile, ivProfile, R.drawable.tab_profile_activated, tvProfile);

        if (!isProfile) {
            isProfile = true;
            Fragment profileFragment = ProfileFragment.Companion.newInstance("DASHBOARD");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_profile, profileFragment);
            ft.commit();
        }
    }

    private void markNotificationAsSelected() {
        selectBottomMenu(frame_notifications, ivNotifications, R.drawable.tab_notification_activated, tvNotifications);

        if (!isNotifications) {
            isNotifications = true;
            Fragment notificationsFragment = NotificationsFragment.newInstance("", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_notifications, notificationsFragment);
            ft.commit();
        }
    }

    private void markPointsAsSelected() {
        selectBottomMenu(frame_points, ivPoints, R.drawable.tab_points_activated, tvPoints);

        if (!isPoints) {
            isPoints = true;
            Fragment pointsFragment = PointsFragment.newInstance("", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_points, pointsFragment);
            ft.commit();
        }
    }

    private boolean shouldShowCancelOrder() {
        final List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments.size() > 0) {
            final Fragment topFragment = fragments.get(fragments.size() - 1);
            final String fragmentName = topFragment.getClass().getName();

            return FRAGMENT_NAMES_TO_GO_HOME.contains(fragmentName);
        }

        return false;
    }

    private void showClearOrderDialog() {
        messages.alert(HomeActivity.this, null, "Are you sure you want to cancel your current Order Process", "Yes", "No", null, new AlertMessages.AlertDialogCallback() {
            @Override
            public void clickedButtonText(String s) {
                if (s.equalsIgnoreCase("Yes")) {
                    clearOrders();
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
}