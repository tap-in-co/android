package com.tapin.tapin.model.business


import com.google.gson.annotations.SerializedName
import com.tapin.tapin.model.resturants.Business

data class AllBusiness(
    @SerializedName("data")
    val `data`: List<Business>,
    @SerializedName("status")
    val status: Int
)