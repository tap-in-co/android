package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Narendra on 5/25/17.
 */

public class CardInfo implements Serializable, Cloneable {
    private static final String DEFAULT_CARD = "1";

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("consumer_cc_info_id")
    public String consumer_cc_info_id;

    @SerializedName("consumer_id")
    public String consumer_id;

    @SerializedName("name_on_card")
    public String name_on_card;

    @SerializedName("cc_no")
    public String cc_no;

    @SerializedName("expiration_date")
    public String expiration_date;

    @SerializedName("cvv")
    public String cvv;

    @SerializedName("zip_code")
    public String zip_code;

    @SerializedName("verified")
    public String verified;

    @SerializedName("default")
    public String defaultCard;

    @SerializedName("card_type")
    public String card_type;

    @SerializedName("timestamp")
    public String timestamp;

    public boolean isDefaultCard() {
        return DEFAULT_CARD.equalsIgnoreCase(defaultCard);
    }
}
