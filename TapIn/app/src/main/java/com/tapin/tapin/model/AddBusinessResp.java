package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by User on 03/10/2017.
 */

public class AddBusinessResp implements Cloneable, Serializable {

    @SerializedName("status")
    public int status;

    @SerializedName("error_message")
    public String error_message;

    @SerializedName("data")
    public BusinessData data;

    public class BusinessData {

        @SerializedName("business_name")
        public String business_name;

        @SerializedName("business_short_name")
        public String business_short_name;


        @SerializedName("user_name")
        public String user_name;

    }

}
