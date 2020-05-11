package com.tapin.tapin.model.market


import com.google.gson.annotations.SerializedName

data class AllMarkets(
    @SerializedName("data")
    val data: List<Market>,
    @SerializedName("success")
    val success: Int
)