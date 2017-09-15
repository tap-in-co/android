package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Narendra on 6/18/17.
 */

public class GetPreviousOrderInfo implements Serializable, Cloneable {

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public List<PreviousOrder> listPreviousOrder;

    public class PreviousOrder implements Serializable,Cloneable{

        @SerializedName("order_item_id")
        public String order_item_id;

        @SerializedName("order_id")
        public String order_id;

        @SerializedName("product_id")
        public String product_id;

        @SerializedName("option_ids")
        public String option_ids;

        @SerializedName("price")
        public String price;

        @SerializedName("quantity")
        public String quantity;

        @SerializedName("item_note")
        public String item_note;

        @SerializedName("product_name")
        public String product_name;

        @SerializedName("product_short_description")
        public String product_short_description;

        @SerializedName("ti_rating")
        public String ti_rating;

        @SerializedName("note")
        public String note;

        @SerializedName("options")
        public List<Options.OptionData> listOptions;

    }

}
