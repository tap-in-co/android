package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Narendra on 4/25/17.
 */

public class OrderInfo implements Serializable,Cloneable{

    @SerializedName("product_id")
    public String product_id;

    @SerializedName("category_icon")
    public String category_icon;

    @SerializedName("product_icon")
    public String product_icon;

    @SerializedName("category_name")
    public String category_name;

    @SerializedName("businessID")
    public String businessID;

    @SerializedName("product_keywords")
    public String product_keywords;

    @SerializedName("SKU")
    public String SKU;

    @SerializedName("name")
    public String name;

    @SerializedName("product_category_id")
    public String product_category_id;

    @SerializedName("short_description")
    public String short_description;

    @SerializedName("long_description")
    public String long_description;

    @SerializedName("availability_status")
    public String availability_status;

    @SerializedName("price")
    public String price;

    @SerializedName("sales_price")
    public String sales_price;

    @SerializedName("sales_start_date")
    public String sales_start_date;

    @SerializedName("sales_end_date")
    public String sales_end_date;

    @SerializedName("pictures")
    public String pictures;

    @SerializedName("detail_information")
    public String detail_information;

    @SerializedName("runtime_fields")
    public String runtime_fields;

    @SerializedName("runtime_fields_detail")
    public String runtime_fields_detail;

    @SerializedName("has_option")
    public String has_option;

    @SerializedName("bought_with_rewards")
    public String bought_with_rewards;

    @SerializedName("more_information")
    public String more_information;

    @SerializedName("ti_rating")
    public int ti_rating;

    @SerializedName("consumer_id")
    public String consumer_id;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("options")
    public List<Options> listOptions;

}
