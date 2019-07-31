package com.tapin.tapin.model.resturants


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Business(
    @SerializedName("active")
    val active: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("beta")
    val beta: String = "",
    @SerializedName("bg_color")
    val bgColor: String = "",
    @SerializedName("bg_image")
    val bgImage: String = "",
    @SerializedName("branch")
    val branch: String = "",
    @SerializedName("business_delivery_id")
    val businessDeliveryId: String = "",
    @SerializedName("businessID")
    val businessID: String = "",
    @SerializedName("business_promotion_id")
    val businessPromotionId: String?,
    @SerializedName("businessTypes")
    val businessTypes: String = "",
    @SerializedName("chat_masters")
    val chatMasters: String = "",
    @SerializedName("chatroom_table")
    val chatroomTable: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("closing_time")
    val closingTime: String?,
    @SerializedName("curr_code")
    val currCode: String = "",
    @SerializedName("curr_symbol")
    val currSymbol: String = "",
    @SerializedName("customerProfileName")
    val customerProfileName: String = "",
    @SerializedName("cycle_time")
    val cycleTime: String = "",
    @SerializedName("date_added")
    val dateAdded: String = "",
    @SerializedName("date_dropped")
    val dateDropped: String = "",
    @SerializedName("delivery_location_charge")
    val deliveryLocationCharge: String = "",
    @SerializedName("delivery_table_charge")
    val deliveryTableCharge: String = "",
    @SerializedName("display_icon_product_categories")
    val displayIconProductCategories: String = "",
    @SerializedName("display_icon_products")
    val displayIconProducts: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("exclusive")
    val exclusive: String = "",
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("inquiry_for_product")
    val inquiryForProduct: String = "",
    @SerializedName("keywords")
    val keywords: String = "",
    @SerializedName("lat")
    val lat: String = "",
    @SerializedName("lng")
    val lng: String = "",
    @SerializedName("map_image_url")
    val mapImageUrl: String = "",
    @SerializedName("marketing_statement")
    val marketingStatement: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("neighborhood")
    val neighborhood: String = "",
    @SerializedName("note")
    val note: String = "",
    @SerializedName("offer_points")
    val offerPoints: String = "",
    @SerializedName("opening_time")
    val openingTime: String?,
    @SerializedName("order_when_closed")
    val orderWhenClosed: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("payment_processing_id")
    val paymentProcessingId: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("pickup_counter_charge")
    val pickupCounterCharge: String = "",
    @SerializedName("pickup_counter_later")
    val pickupCounterLater: String = "",
    @SerializedName("pickup_location_charge")
    val pickupLocationCharge: String = "",
    @SerializedName("pictures")
    var pictures: String = "",
    @SerializedName("process_time")
    val processTime: String = "",
    @SerializedName("promotion_code")
    val promotionCode: String?,
    @SerializedName("promotion_discount_amount")
    val promotionDiscountAmount: String?,
    @SerializedName("promotion_message")
    val promotionMessage: String?,
    @SerializedName("rating")
    val rating: String = "",
    @SerializedName("service_charge")
    val serviceCharge: String = "",
    @SerializedName("short_name")
    val shortName: String = "",
    @SerializedName("should_tip")
    val shouldTip: String = "",
    @SerializedName("sms_no")
    val smsNo: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("stripe_password")
    val stripePassword: String = "",
    @SerializedName("stripe_secret_key")
    val stripeSecretKey: String = "",
    @SerializedName("stripe_username")
    val stripeUsername: String = "",
    @SerializedName("sub_businesses")
    val subBusinesses: String = "",
    @SerializedName("tax_rate")
    val taxRate: String = "",
    @SerializedName("text_color")
    val textColor: String = "",
    @SerializedName("ti_rating")
    val tiRating: String?,
    @SerializedName("time_interval")
    val timeInterval: String = "",
    @SerializedName("timestamp")
    val timestamp: String = "",
    @SerializedName("username")
    val username: String = "",
    @SerializedName("validate_chat")
    val validateChat: String = "",
    @SerializedName("website")
    val website: String = "",
    @SerializedName("zipcode")
    val zipcode: String = ""
) : Serializable, Cloneable