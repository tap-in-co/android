package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Narendra on 5/6/17.
 */

public class LocationInfo implements Serializable, Cloneable {

    @SerializedName("delivery_main_location_id")
    public String delivery_main_location_id;

    @SerializedName("delivery_id")
    public String delivery_id;

    @SerializedName("main_location_name")
    public String main_location_name;

    @SerializedName("main_location_name_abrv")
    public String main_location_name_abrv;

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

    @SerializedName("locations")
    public List<Location> listLocation;

    @SerializedName("delivery_locations_charge")
    public String delivery_locations_charge;

}
