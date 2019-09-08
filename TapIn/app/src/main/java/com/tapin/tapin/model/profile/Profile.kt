package com.tapin.tapin.model.profile

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("age_group")
    val ageGroup: Int,
    @SerializedName("app_ver")
    val appVer: String,
    @SerializedName("cmd")
    val cmd: String,
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_work")
    val emailWork: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("sms_no")
    val smsNo: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("zipcode")
    val zipcode: String
)

data class ProfileResponse(
    @SerializedName("age_group")
    val ageGroup: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email1")
    val email1: String,
    @SerializedName("email2")
    val email2: String,
    @SerializedName("new_user")
    val newUser: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("qrcode_file")
    val qrcodeFile: String,
    @SerializedName("sms_no")
    val smsNo: String,
    @SerializedName("status_code")
    val statusCode: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("zipcode")
    val zipcode: String
) {
    fun isValidProfile(): Boolean = email1.isNotEmpty()
}

data class GetProfileRequest(
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("uuid")
    val uuid: String
)