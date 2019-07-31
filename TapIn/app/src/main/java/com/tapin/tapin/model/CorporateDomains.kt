package com.tapin.tapin.model

import com.google.gson.annotations.SerializedName

data class CorporateDomains(
    @SerializedName("data")
    val data: List<CorporateDomain>,
    @SerializedName("success")
    val success: Int
)

data class CorporateDomain(
    @SerializedName("active")
    val active: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("beta")
    val beta: String,
    @SerializedName("building_name")
    val building_name: String,
    @SerializedName("corp_id")
    val corpId: String,
    @SerializedName("corp_name")
    val corpName: String,
    @SerializedName("cutoff_time")
    val cutoffTime: String,
    @SerializedName("delivery_charge")
    val deliveryCharge: String,
    @SerializedName("delivery_location")
    val deliveryLocation: String,
    @SerializedName("delivery_time")
    val deliveryTime: String,
    @SerializedName("delivery_week_days")
    val deliveryWeekDays: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("driver_pickup_time")
    val driverPickupTime: String,
    @SerializedName("location_abbr")
    val locationAbbr: String,
    @SerializedName("merchant_ids")
    val merchantIds: String,
    @SerializedName("password")
    val password: Any,
    @SerializedName("user_id")
    val userId: Any
)