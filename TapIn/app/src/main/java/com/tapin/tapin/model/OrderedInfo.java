package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 5/22/17.
 */

public class OrderedInfo implements Serializable, Cloneable {

    public String businessID;

    public String item_note;

    public String note;

    public double price = 0.0;

    public double points = 0.0;

    public String product_id;

    public String product_description;

    public String product_imageurl;

    public String product_option = "";

    public String product_order_id;

    public String product_name;

    public List<String> listOptions = new ArrayList<>();

    public int quantity = 1;

    public String selected_ProductID_array = "";

    public float ti_rating;

}
