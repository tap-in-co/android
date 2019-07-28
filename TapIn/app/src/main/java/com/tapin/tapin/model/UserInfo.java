package com.tapin.tapin.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Linux on 4/4/2017.
 */

public class UserInfo implements Serializable {
    @SerializedName("zipcode")
    public String zipcode;
    @SerializedName("uid")
    public String uid;
    @SerializedName("email2")
    public String email2;
    @SerializedName("new_user")
    public String new_user;
    @SerializedName("email1")
    public String email1;
    @SerializedName("status_code")
    public String status_code;
    @SerializedName("sms_no")
    public String sms_no;
    @SerializedName("dob")
    public String dob;
    @SerializedName("age_group")
    public String age_group;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("qrcode_file")
    public String qrcode_file;

    @NonNull
    @Override
    public String toString() {
        return "User Info:: uid:" + uid + ", email: " + email1 + ", email2 :" + email2;
    }
}
