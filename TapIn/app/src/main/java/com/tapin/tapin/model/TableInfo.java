package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Narendra on 5/6/17.
 */

public class TableInfo implements Serializable,Cloneable{

    @SerializedName("delivery_table_id")
    public String delivery_table_id;

    @SerializedName("business_id")
    public String business_id;

    @SerializedName("table_no_min")
    public String table_no_min;

    @SerializedName("table_no_max")
    public String table_no_max;

    @SerializedName("message_to_consumers")
    public String message_to_consumers;

    @SerializedName("main_location_map")
    public String main_location_map;

    @SerializedName("delivery_charge")
    public String delivery_charge;

    @SerializedName("delivery_start_time")
    public String delivery_start_time;

    @SerializedName("delivery_end_time")
    public String delivery_end_time;

    @SerializedName("delivery_time_interval_in_minutes")
    public String delivery_time_interval_in_minutes;

    @SerializedName("note")
    public String note;

}
