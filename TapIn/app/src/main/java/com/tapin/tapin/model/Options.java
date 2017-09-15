package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Narendra on 4/25/17.
 */

public class Options implements Serializable, Cloneable {

    @SerializedName("option_category_name")
    public String option_category_name;

    @SerializedName("only_choose_one")
    public String only_choose_one;

    @SerializedName("optionData")
    public List<OptionData> optionData;

    public class OptionData implements Serializable, Cloneable {

        @SerializedName("option_id")
        public String option_id;

        @SerializedName("name")
        public String name;

        @SerializedName("price")
        public String price;

        @SerializedName("description")
        public String description;

        @SerializedName("availability_status")
        public String availability_status;

        public boolean isSelected;

    }
}
