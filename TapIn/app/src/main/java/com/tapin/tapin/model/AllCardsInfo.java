package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Narendra on 6/9/17.
 */

public class AllCardsInfo implements Serializable, Cloneable {

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public List<CardInfo> listCards;

}
