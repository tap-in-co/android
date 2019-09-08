package com.tapin.tapin.network

import com.tapin.tapin.model.profile.CardDetailsResponse
import com.tapin.tapin.model.profile.GetProfileRequest
import com.tapin.tapin.model.profile.ProfileRequest
import com.tapin.tapin.model.profile.ProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("profilesystem/consumerprofile.php")
    fun createProfile(@Body profile: ProfileRequest): Call<ProfileResponse>

    @POST("profilesystem/consumerprofile.php")
    suspend fun getProfile(@Body profile: GetProfileRequest): ProfileResponse

    @GET("include/model.php")
    suspend fun getCardDetails(@Query("cmd") cmd: String = "get_consumer_default_cc", @Query("consumer_id") id : String): CardDetailsResponse
}