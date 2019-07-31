package com.tapin.tapin.model

import com.google.gson.annotations.SerializedName
import com.tapin.tapin.model.resturants.Business

import java.io.Serializable

/**
 * Created by User on 03/10/2017.
 */

class BusinessInfo : Cloneable, Serializable {

    @SerializedName("status")
    var status: Int = 0

    @SerializedName("data")
    var listBusinesses: List<Business>? = null
}
