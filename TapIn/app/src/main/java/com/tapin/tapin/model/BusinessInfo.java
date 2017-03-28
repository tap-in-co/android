package com.tapin.tapin.model;

import java.io.Serializable;

/**
 * Created by User on 03/10/2017.
 */

public class BusinessInfo implements Cloneable, Serializable {
    public String name;
    public String type;
    public String keywords;
    public String icon;
    public String city;
    public double rating = 0.0;
    public double lat;
    public double lng;
    public String address;
    public String businessID;
    public String branch;
    public String businessTypes;
    public String customerProfileName;
    public String inquiry_for_product;
    public String map_image_url;
    public String website;
    public String email;
    public String chatroom_table;
    public String chat_masters;
    public String validate_chat;
    public String phone;
    public String sms_no;
    public String state;
    public String zipcode;
    public String neighborhood;
    public String active;
    public String payment_processing_id;
    public String payment_processing_email;
    public String should_tip;
    public String date_added;
    public String date_dropped;
    public String external_reference_no;
    public String note;
    public String pictures;
    public String exclusive;
    public String short_name;
    public String bg_image;
    public String text_color;
    public String bg_color;
    public String marketing_statement;
    public String process_time;
    public String username;
    public String password;
    public String stripe_secret_key;
    public String stripe_username;
    public String stripe_password;
    public String timestamp;
    public String iDeviceNotificationUUID;
    public String beta;
    public String business_delivery_id;
    public String sub_businesses;
    public String display_icon_products;
    public String display_icon_product_categories;
    public String pickup_later;
    public String promotion_discount_amount;
    public String promotion_message;
    public String promotion_code;
    public String business_promotion_id;
    public String opening_time;
    public String closing_time;
    public String ti_rating;



    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
