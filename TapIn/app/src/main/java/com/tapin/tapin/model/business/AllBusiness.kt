package com.tapin.tapin.model.business


import com.google.gson.annotations.SerializedName

data class AllBusiness(
    @SerializedName("data")
    val `data`: List<Business>,
    @SerializedName("status")
    val status: Int
)