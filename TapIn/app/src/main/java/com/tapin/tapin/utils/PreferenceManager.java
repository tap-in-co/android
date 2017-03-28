package com.tapin.tapin.utils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import com.tapin.tapin.R;


public class PreferenceManager extends Application {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor prefEditor;

    DisplayMetrics displaymetrics;

    public static final String APP_ID = "30301";
    public static final String AUTH_KEY = "HWNLh8WUzCksBJ7";
    public static final String AUTH_SECRET = "ZCE9QcbnMUn9d3U";
    public static final String ACCOUNT_KEY = "ymqyQAPnm7yHxZ62iykW";


//    public static boolean isTesting = false;


    public static String getEmail() {

        return preferences.getString("email", "");
    }

    @Override
    public void onCreate() {

        super.onCreate();
        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        prefEditor = preferences.edit();

        prefEditor.commit();
        instance = this;

    }

    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }


    public static boolean isNotification1Hour() {
        return preferences.getBoolean("hour1", true);
    }

    public static void setIsNotification1Hour(boolean bool) {
        prefEditor.putBoolean("hour1", bool);
        prefEditor.commit();
    }

    public static boolean isNotification1Day() {
        return preferences.getBoolean("day1", true);
    }

    public static void setIsNotification1Day(boolean bool) {
        prefEditor.putBoolean("day1", bool);
        prefEditor.commit();
    }

    public static String getFullName() {
        return preferences.getString("full_name", "");
    }

    public static void putFullname(String lname) {
        prefEditor.putString("full_name", lname);
        prefEditor.commit();
    }

    public static String getUserId() {
        return preferences.getString("id", "");
    }

    public static void putUserId(String uid) {
        prefEditor.putString("id", uid);
        prefEditor.commit();
    }


    public static String getFirstName() {
        return preferences.getString("first_name", "");
    }

    public static void putFirstname(String fname) {
        prefEditor.putString("first_name", fname);
        prefEditor.commit();
    }

    public static String getLastName() {
        return preferences.getString("last_name", "");
    }

    public static void putLastname(String lname) {
        prefEditor.putString("last_name", lname);
        prefEditor.commit();
    }

    public static void putEmail(String email) {
        prefEditor.putString("email", email);
        prefEditor.commit();
    }

    public static String getGender() {
        return preferences.getString("gender", "");
    }

    public static void putGender(String gender) {
        prefEditor.putString("gender", gender);
        prefEditor.commit();
    }

    public static String getProfilePic() {
//        return Utils.getProfilePic(PreferenceManager.getUserId());
        return preferences.getString("profile_pic", "");
    }

    public static void putProfilePic(String profilePic) {
        prefEditor.putString("profile_pic", profilePic);
        prefEditor.commit();
    }

    public static void putStudentType(String stype) {
        prefEditor.putString("student_type", stype);
        prefEditor.commit();
    }

    public static String getStudentType() {
        return preferences.getString("student_type", "");
    }

    public static String getDegreeType() {
        return preferences.getString("degree_type", "");
    }

    public static void putDegreeType(String dtype) {
        prefEditor.putString("degree_type", dtype);

    }

    public static String getPhone() {
        return preferences.getString("phone", "");
    }

    public static void putPhone(String phone) {
        prefEditor.putString("phone", phone);

    }

    public static String getCreatedDate() {
        return preferences.getString("created", "");
    }

    public static void putCreated(String created) {
        prefEditor.putString("created", created);

    }

    public static int isUserVerified() {
        return preferences.getInt("is_verified", 0);
    }

    public static void putIsUserVerified(int isVerified) {
        prefEditor.putInt("is_verified", isVerified);

    }

    public static String getLastdate() {
        return preferences.getString("last_date", "");
    }

    public static boolean isShowSuggestedOrganization() {
        return preferences.getBoolean("isShowSuggestedOrganization", true);
    }


    public static void putIsShowSuggestedOrganization(boolean bool) {
        prefEditor.putBoolean("isShowSuggestedOrganization", bool);
        prefEditor.commit();
    }


    public static String getUsername() {
        return preferences.getString("user_name", "");
    }

    public static void putUsername(String uniid) {
        prefEditor.putString("user_name", uniid);

    }

    public static String getTokenType() {
        return preferences.getString("token_type", "");
    }

    public static void putTokenType(String uniid) {
        prefEditor.putString("token_type", uniid);

    }

    public static void putAccessToken(String access_token) {
        prefEditor.putString("access_token", access_token);
    }

    public static String getAccessToken() {
        return preferences.getString("access_token", "");
    }

    public static void putLastActive(String access_token) {
        prefEditor.putString("last_active", access_token);
    }

    public static String getLastActive() {
        return preferences.getString("last_active", "");
    }

    public static void putQBLogin(String qb_login) {
        prefEditor.putString("qb_login", qb_login);
        prefEditor.commit();
    }

    public static String getQBId() {
        return preferences.getString("qb_id", "");
    }

    public static void putQBId(String qb_password) {
        prefEditor.putString("qb_id", qb_password);
        prefEditor.commit();
    }

    public static String getQBLogin() {
        return preferences.getString("qb_login", "");
    }

    public static void putQBPassword(String qb_password) {
        prefEditor.putString("qb_password", qb_password);
        prefEditor.commit();
    }

    public static String getQBPassword() {
        return preferences.getString("qb_password", "");
    }

    public static void putRating(String rating) {
        prefEditor.putString("rating", rating);
    }

    public static String getRating() {
        return preferences.getString("rating", "");
    }

    public static void putCity(String city) {
        prefEditor.putString("city", city);
    }

    public static String getCity() {
        return preferences.getString("city", "");
    }

    public static void putState(String state) {
        prefEditor.putString("state", state);
        prefEditor.commit();
    }

    public static String getState() {
        return preferences.getString("state", "");
    }

    public static String getCountry() {
        return preferences.getString("country", "");
    }

    public static void putCountry(String country) {
        prefEditor.putString("country", country);
        prefEditor.commit();
    }

    public static String getBirthday() {
        return preferences.getString("birthday", "");
    }

    public static void putBirthday(String birthday) {
        prefEditor.putString("birthday", birthday);
        prefEditor.commit();
    }

    public static String getTagline() {
        return preferences.getString("tagline", "");
    }

    public static void putTagline(String tagline) {

        prefEditor.putString("tagline", tagline);
        prefEditor.commit();
    }

    public static String getDescription() {
        return preferences.getString("description", "");
    }

    public static void putDescription(String description) {
        prefEditor.putString("description", description);
        prefEditor.commit();
    }

    public static void putRegId(String regId) {
        prefEditor.putString("regId", regId);
        prefEditor.commit();
    }

    public static String getFundingType() {
        return preferences.getString("funding_type", "");
    }

    public static void putFundingType(String tagline) {

        prefEditor.putString("funding_type", tagline);
        prefEditor.commit();
    }
    public static String getSavedLocation() {
        return preferences.getString("location", "");
    }

    public static void putSavedLocation(String location) {

        prefEditor.putString("location", location);
        prefEditor.commit();
    }

    public static String getSavedLocationLatitude() {
        return preferences.getString("location_latitude", "");
    }

    public static void putSavedLocationLatitude(String location_latitude) {

        prefEditor.putString("location_latitude", location_latitude);
        prefEditor.commit();
    }

    public static String getSavedLocationLongitude() {
        return preferences.getString("location_longitude", "");
    }

    public static void putSavedLocationLongitude(String location_longitude) {

        prefEditor.putString("location_longitude", location_longitude);
        prefEditor.commit();
    }


    public static String getRegId() {
        return preferences.getString("regId", "");
    }

//    public static void setUserData(UserInfo userInfo) {
//
//
//        putFirstname(userInfo.first_name);
//        putLastname(userInfo.last_name);
//        if (Utils.isNotEmpty(userInfo.username))
//            putUsername(userInfo.username);
//        putEmail(userInfo.email);
//        if (Utils.isNotEmpty(userInfo.token_type))
//            putTokenType(userInfo.token_type);
//        if (Utils.isNotEmpty(userInfo.access_token)) {
//            putAccessToken(userInfo.access_token);
//        }
//        putLastActive(userInfo.last_active);
//        if (Utils.isNotEmpty(userInfo.qb_login))
//            putQBLogin(userInfo.qb_login);
//        if (Utils.isNotEmpty(userInfo.qb_password))
//            putQBPassword(userInfo.qb_password);
//        putRating(userInfo.rating);
//        if (Utils.isNotEmpty(userInfo.user_id)) {
//            putUserId(userInfo.user_id + "");
//        }
//        putProfilePic(userInfo.avatar);
//        putFullname(userInfo.full_name);
//        putCity(userInfo.city);
//        putState(userInfo.state);
//        putCountry(userInfo.country);
//        putTagline(userInfo.tag_line);
//        putBirthday(userInfo.birthday);
//        putDescription(userInfo.description);
//        putPhone(userInfo.phone_number);
//        prefEditor.putString("userInfo", new Gson().toJson(userInfo).toString());
//
//        prefEditor.commit();
//    }
//
//    public static void putNotificationData(UserInfo userInfoData) {
//
//
//        UserInfo userInfo = getUserInfo();
//        if (userInfo != null) {
//            userInfo.notify_task_assigned = userInfoData.notify_task_assigned;
//            userInfo.notify_task_bids = userInfoData.notify_task_bids;
//            userInfo.push_notify_allowed = userInfoData.push_notify_allowed;
//            userInfo.listCategoryNotifications = userInfoData.listCategoryNotifications;
//
//            prefEditor.putString("userInfo", new Gson().toJson(userInfo).toString());
//            prefEditor.commit();
//
//        }
//
//
//    }


//
//    public static UserInfo getUserInfo() {
//        UserInfo userInfo = null;
//        String content = preferences.getString("userInfo", "");
//        if (Utils.isEmpty(content))
//            return userInfo;
//        try {
//            Type type = new TypeToken<UserInfo>() {
//            }.getType();
//            userInfo = new Gson().fromJson(content,
//                    type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userInfo;
//    }

    public static void resetAll() {

        prefEditor.clear();
        prefEditor.commit();

    }

    private static PreferenceManager instance;

    public static PreferenceManager getInstance() {
        return instance;
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
