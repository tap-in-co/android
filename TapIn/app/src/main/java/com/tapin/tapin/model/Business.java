package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by User on 03/10/2017.
 */

public class Business implements Cloneable, Serializable {

    @SerializedName("name")
    public String name;

    @SerializedName("businessID")
    public String businessID;

    @SerializedName("branch")
    public String branch;

    @SerializedName("businessTypes")
    public String businessTypes;

    @SerializedName("customerProfileName")
    public String customerProfileName;

    @SerializedName("inquiry_for_product")
    public String inquiry_for_product;

    @SerializedName("icon")
    public String icon;

    @SerializedName("map_image_url")
    public String map_image_url;

    @SerializedName("website")
    public String website;

    @SerializedName("email")
    public String email;

    @SerializedName("chatroom_table")
    public String chatroom_table;

    @SerializedName("chat_masters")
    public String chat_masters;

    @SerializedName("validate_chat")
    public String validate_chat;

    @SerializedName("rating")
    public double rating = 0.0;

    @SerializedName("phone")
    public String phone;

    @SerializedName("sms_no")
    public String sms_no;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("state")
    public String state;

    @SerializedName("zipcode")
    public String zipcode;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

    @SerializedName("active")
    public String active;

    @SerializedName("payment_processing_id")
    public String payment_processing_id;

    @SerializedName("should_tip")
    public String should_tip;

    @SerializedName("date_added")
    public String date_added;

    @SerializedName("date_dropped")
    public String date_dropped;

    @SerializedName("note")
    public String note;

    @SerializedName("pictures")
    public String pictures;

    @SerializedName("exclusive")
    public String exclusive;

    @SerializedName("short_name")
    public String short_name;

    @SerializedName("bg_image")
    public String bg_image;

    @SerializedName("text_color")
    public String text_color;

    @SerializedName("bg_color")
    public String bg_color;

    @SerializedName("marketing_statement")
    public String marketing_statement;

    @SerializedName("process_time")
    public String process_time;

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("stripe_secret_key")
    public String stripe_secret_key;

    @SerializedName("stripe_username")
    public String stripe_username;

    @SerializedName("stripe_password")
    public String stripe_password;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("keywords")
    public String keywords;

    @SerializedName("beta")
    public String beta;

    @SerializedName("sub_businesses")
    public String sub_businesses;

    @SerializedName("display_icon_product")
    public String display_icon_products;

    @SerializedName("display_icon_product_categories")
    public String display_icon_product_categories;

    @SerializedName("tax_rate")
    public String tax_rate;

    @SerializedName("pickup_counter_charge")
    public String pickup_counter_charge;

    @SerializedName("pickup_location_charge")
    public String pickup_location_charge;

    @SerializedName("delivery_table_charge")
    public String delivery_table_charge;

    @SerializedName("curr_code")
    public String curr_code;

    @SerializedName("curr_symbol")
    public String curr_symbol;

    @SerializedName("delivery_location_charge")
    public String delivery_location_charge;

    @SerializedName("promotion_discount_amount")
    public String promotion_discount_amount;

    @SerializedName("promotion_message")
    public String promotion_message;

    @SerializedName("promotion_code")
    public String promotion_code;

    @SerializedName("business_promotion_id")
    public String business_promotion_id;

    @SerializedName("opening_time")
    public String opening_time;

    @SerializedName("closing_time")
    public String closing_time;

    @SerializedName("ti_rating")
    public String ti_rating;

}
