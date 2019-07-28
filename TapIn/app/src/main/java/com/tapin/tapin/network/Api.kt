package com.tapin.tapin.network

import com.tapin.tapin.model.profile.ProfileRequest
import com.tapin.tapin.model.profile.ProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("profilesystem/consumerprofile.php")
    fun createProfile(@Body profile: ProfileRequest): Call<ProfileResponse>
}