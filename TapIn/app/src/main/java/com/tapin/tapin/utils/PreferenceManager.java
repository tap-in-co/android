package com.tapin.tapin.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapin.tapin.R;
import com.tapin.tapin.model.CorporateDomain;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.UserInfo;
import com.tapin.tapin.network.Api;
import com.tapin.tapin.network.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;


public class PreferenceManager extends Application {
    public static final String APP_ID = "30301";
    public static final String AUTH_KEY = "HWNLh8WUzCksBJ7";
    public static final String AUTH_SECRET = "ZCE9QcbnMUn9d3U";
    public static final String ACCOUNT_KEY = "ymqyQAPnm7yHxZ62iykW";
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor prefEditor;
    private static PreferenceManager instance;

    private CorporateDomain selectedCorporateDomain = null;

    // Get The API interface for all the apis
    private Api api = null;

    // Is it a Corporate Order
    private boolean isCorporateOrder = false;
    private String corporateOrderMerchantIds = "";

    @NonNull
    public static PreferenceManager getInstance() {
        return instance;
    }

    public CorporateDomain getSelectedCorporateDomain() {
        return selectedCorporateDomain;
    }

    public void setSelectedCorporateDomain(CorporateDomain selectedCorporateDomain) {
        this.selectedCorporateDomain = selectedCorporateDomain;
    }

    public static String getEmail() {

        return preferences.getString("email", "");
    }

    public static String getUserId() {
        return preferences.getString("id", "");
    }

    public static void putUserId(String uid) {
        prefEditor.putString("id", uid);
        prefEditor.commit();
    }

    public static void putEmail(String email) {
        prefEditor.putString("email", email);
        prefEditor.commit();
    }

    public static void putWorkEmail(String workEmail) {
        prefEditor.putString("workEmail", workEmail);
        prefEditor.commit();
    }

    public static String getWorkEmail() {
        return preferences.getString("workEmail", "");
    }

    public static void putZipcode(String zipcode) {
        prefEditor.putString("zipcode", zipcode);
        prefEditor.commit();
    }

    public static String getZipcode() {
        return preferences.getString("zipcode", "");
    }

    public static String getPhone() {
        return preferences.getString("phone", "");
    }

    public static void putPhone(String phone) {
        prefEditor.putString("phone", phone);
    }

    public static String getUsername() {
        return preferences.getString("user_name", "");
    }

    public static void putUsername(String uniid) {
        prefEditor.putString("user_name", uniid);
    }

    public static void putBirthday(String birthday) {
        prefEditor.putString("birthday", birthday);
        prefEditor.commit();
    }

    public static void putAgeGroup(String age_group) {

        prefEditor.putString("age_group", age_group);
        prefEditor.commit();
    }

    public static void putDescription(String description) {
        prefEditor.putString("description", description);
        prefEditor.commit();
    }

    public static String getNotificationLink() {
        return preferences.getString("notification_link", "");
    }

    public static void putNotificationLink(String notification_link) {

        prefEditor.putString("notification_link", notification_link);
        prefEditor.commit();
    }

    public static boolean getIsSubmitRestaurent() {
        return preferences.getBoolean("submit_restaurent", false);
    }

    public static void putIsSubmitRestaurent(boolean submit_restaurent) {

        prefEditor.putBoolean("submit_restaurent", submit_restaurent);
        prefEditor.commit();
    }

    public static String getWorkEmailDomain() {
        String email = preferences.getString("workEmail", "");
        String fullDomain = email.substring(email.indexOf("@") + 1);
        return fullDomain.substring(0, fullDomain.indexOf("."));
    }

    public static void setUserData(UserInfo userInfo) {

        if (Utils.isNotEmpty(userInfo.nickname))
            putUsername(userInfo.nickname);

        if (Utils.isNotEmpty(userInfo.email1))
            putEmail(userInfo.email1);

        if (Utils.isNotEmpty(userInfo.uid)) {
            putUserId(userInfo.uid);
        }

        if (Utils.isNotEmpty(userInfo.email2)) {
            putWorkEmail(userInfo.email2);
        }

        if (Utils.isNotEmpty(userInfo.age_group))
            putAgeGroup(userInfo.age_group);

        if (Utils.isNotEmpty(userInfo.dob)) {
            putBirthday(userInfo.dob);
        }

        if (Utils.isNotEmpty(userInfo.qrcode_file)) {
            putDescription(userInfo.qrcode_file);
        }

        if (Utils.isNotEmpty(userInfo.sms_no))
            putPhone(userInfo.sms_no);

        if (Utils.isNotEmpty(userInfo.zipcode))
            putZipcode(userInfo.zipcode);

        prefEditor.putString("userInfo", new Gson().toJson(userInfo));

        prefEditor.commit();

    }

    public static void saveCardData(String json) {
        prefEditor.putString("CARD_DATA", json);
        prefEditor.commit();
    }

    @Nullable
    public static UserInfo getUserInfo() {
        UserInfo userInfo = null;
        String content = preferences.getString("userInfo", null);

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
            prefEditor.putString("PointsData", new Gson().toJson(getPointsResp));
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

    @Override
    public void onCreate() {
        super.onCreate();

        api = new Service().getClient().create(Api.class);

        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        prefEditor = preferences.edit();

        prefEditor.commit();
        instance = this;
    }

    @NonNull
    public Api getApi() {
        return api;
    }

    public boolean isCorporateOrder() {
        return isCorporateOrder;
    }

    public void setCorporateOrder(boolean corporateOrder) {
        isCorporateOrder = corporateOrder;
    }

    public String getCorporateOrderMerchantIds() {
        return corporateOrderMerchantIds;
    }

    public void setCorporateOrderMerchantIds(String corporateOrderMerchantId) {
        try {
            corporateOrderMerchantIds = URLEncoder.encode(corporateOrderMerchantId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            corporateOrderMerchantIds = "";
        }
        Log.d("Hello", corporateOrderMerchantIds);
    }
}
