package com.tapin.tapin.model.market


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Market(
    @SerializedName("active")
    val active: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("beta")
    val beta: String,
    @SerializedName("building_name")
    val buildingName: String,
    @SerializedName("corp_id")
    val corpId: String,
    @SerializedName("corp_name")
    val corpName: String,
    @SerializedName("cutoff_date")
    val cutoffDate: String,
    @SerializedName("cutoff_no_days")
    val cutoffNoDays: String,
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
    @SerializedName("description")
    val description: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("driver_pickup_time")
    val driverPickupTime: String,
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("interactive_map")
    val interactiveMap: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("location_abbr")
    val locationAbbr: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("market_open_hours")
    val marketOpenHours: String,
    @SerializedName("marketing_statement")
    val marketingStatement: String,
    @SerializedName("merchant_ids")
    val merchantIds: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("pickup_date")
    val pickupDate: String,
    @SerializedName("pictures")
    val pictures: String,
    @SerializedName("service_charge_limit")
    val serviceChargeLimit: String,
    @SerializedName("tax_rate")
    val taxRate: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("website")
    val website: String
): Serializable