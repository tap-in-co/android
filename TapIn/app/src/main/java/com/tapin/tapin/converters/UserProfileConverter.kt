package com.tapin.tapin.converters

import com.tapin.tapin.model.UserInfo
import com.tapin.tapin.model.profile.ProfileResponse

fun ProfileResponse.toUserInfo(): UserInfo {
    val userInfo = UserInfo()

    // The Mapping
    userInfo.email2 = this.email2
    userInfo.email1 = this.email1
    userInfo.age_group = this.ageGroup
    userInfo.dob = this.dob
    userInfo.new_user = this.newUser.toString()
    userInfo.nickname = this.nickname
    userInfo.qrcode_file = this.qrcodeFile
    userInfo.sms_no = this.smsNo
    userInfo.status_code = this.statusCode
    userInfo.uid = this.uid
    userInfo.zipcode = this.zipcode

    return userInfo
}