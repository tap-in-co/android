package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Narendra on 5/6/17.
 */

public class Location implements Serializable, Cloneable {

    @SerializedName("delivery_locations_id")
    public String delivery_locations_id;

    @SerializedName("delivery_main_location_id")
    public String delivery_main_location_id;

    @SerializedName("location_name")
    public String location_name;

    @SerializedName("location_abrv")
    public String location_abrv;

    @SerializedName("note")
    public String note;

    @SerializedName("external_delivery_location_id")
    public String external_delivery_location_id;

    @SerializedName("location_address")
    public String location_address;

}