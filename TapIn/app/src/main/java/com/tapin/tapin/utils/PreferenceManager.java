package com.tapin.tapin.utils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapin.tapin.R;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.UserInfo;

import org.json.JSONObject;

import java.lang.reflect.Type;


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

    public static void putZipcode(String zipcode) {
        prefEditor.putString("zipcode", zipcode);
        prefEditor.commit();
    }

    public static String getZipcode() {
        return preferences.getString("zipcode", "");
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

    public static String getAgeGroup() {
        return preferences.getString("age_group", "");
    }

    public static void putAgeGroup(String age_group) {

        prefEditor.putString("age_group", age_group);
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

    public static String getNotificationLink() {
        return preferences.getString("notification_link", "");
    }

    public static void putNotificationLink(String notification_link) {

        prefEditor.putString("notification_link", notification_link);
        prefEditor.commit();
    }

    public static String getSavedLocation() {
        return preferences.getString("location", "");
    }

    public static void putSavedLocation(String location) {

        prefEditor.putString("location", location);
        prefEditor.commit();
    }

    public static boolean getIsSubmitRestaurent() {
        return preferences.getBoolean("submit_restaurent", false);
    }

    public static void putIsSubmitRestaurent(boolean submit_restaurent) {

        prefEditor.putBoolean("submit_restaurent", submit_restaurent);
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

    public static void setUserData(UserInfo userInfo) {

        if (Utils.isNotEmpty(userInfo.nickname))
            putUsername(userInfo.nickname);

        if (Utils.isNotEmpty(userInfo.email1))
            putEmail(userInfo.email1);

        if (Utils.isNotEmpty(userInfo.uid)) {
            putUserId(userInfo.uid);
        }
        if (Utils.isNotEmpty(userInfo.age_group))
            putAgeGroup(userInfo.age_group);

        putBirthday(userInfo.dob);
        putDescription(userInfo.qrcode_file);

        if (Utils.isNotEmpty(userInfo.sms_no))
            putPhone(userInfo.sms_no);

        if (Utils.isNotEmpty(userInfo.zipcode))
            putZipcode(userInfo.zipcode);

        prefEditor.putString("userInfo", new Gson().toJson(userInfo).toString());

        prefEditor.commit();

    }

    public static void saveCardData(String json) {

        prefEditor.putString("CARD_DATA", json);
        prefEditor.commit();

    }

    public static String getCardData() {

        return preferences.getString("CARD_DATA", null);

    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = null;
        String content = preferences.getString("userInfo", null);
        if (Utils.isEmpty(content))
            return userInfo;
        try {
            Type type = new TypeToken<UserInfo>() {
            }.getType();
            userInfo = new Gson().fromJson(content,
                    type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    public static void putPointsData(GetPointsResp getPointsResp) {

        if (getPointsResp != null) {

            prefEditor.putString("PointsData", new Gson().toJson(getPointsResp).toString());
            prefEditor.commit();

        }


    }

    public static GetPointsResp getPointsData() {

        GetPointsResp userInfo = null;
        String content = preferences.getString("PointsData", "");
        if (Utils.isEmpty(content))
            return userInfo;
        try {
            Type type = new TypeToken<GetPointsResp>() {
            }.getType();
            userInfo = new Gson().fromJson(content,
                    type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;

    }


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
