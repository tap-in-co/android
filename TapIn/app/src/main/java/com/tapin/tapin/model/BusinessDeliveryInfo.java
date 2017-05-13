package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Narendra on 5/6/17.
 */

public class BusinessDeliveryInfo implements Serializable,Cloneable{

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("table")
    public TableInfo tableInfo;

    @SerializedName("location_info")
    public LocationInfo locationInfo;

}
