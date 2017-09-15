package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 03/10/2017.
 */

public class BusinessInfo implements Cloneable, Serializable {

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public List<Business> listBusinesses;
}
