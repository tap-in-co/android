package com.tapin.tapin.model.profile


import com.google.gson.annotations.SerializedName

data class CardDetailsResponse(
    @SerializedName("data")
    val data: List<CardDetails>,
    @SerializedName("status")
    val status: Int
)

data class CardDetails(
    @SerializedName("card_type")
    val cardType: String,
    @SerializedName("cc_no")
    val ccNo: String,
    @SerializedName("consumer_cc_info_id")
    val consumerCcInfoId: String,
    @SerializedName("consumer_id")
    val consumerId: String,
    @SerializedName("cvv")
    val cvv: String,
    @SerializedName("default")
    val default: String,
    @SerializedName("expiration_date")
    val expirationDate: String,
    @SerializedName("name_on_card")
    val nameOnCard: String,
    @SerializedName("stripe_card_id")
    val stripeCardId: String,
    @SerializedName("stripe_fingerprint")
    val stripeFingerprint: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("verified")
    val verified: String,
    @SerializedName("zip_code")
    val zipCode: String
)