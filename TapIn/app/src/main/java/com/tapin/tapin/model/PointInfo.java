package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Linux on 4/4/2017.
 */

public class PointInfo implements Serializable {

    @SerializedName("consumer_id")
    public String consumer_id;

    @SerializedName("business_id")
    public String business_id;


    @SerializedName("points_reason_id")
    public String points_reason_id;


    @SerializedName("points")
    public String points;

    @SerializedName("order_id")
    public String order_id;

    @SerializedName("time_earned")
    public String time_earned;


    @SerializedName("time_redeemed")
    public String time_redeemed;

     @SerializedName("activity_time")
    public String activity_time;


}
