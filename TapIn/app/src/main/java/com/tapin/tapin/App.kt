package com.tapin.tapin

import android.app.Application
import com.tapin.tapin.model.profile.CardDetailsResponse
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.network.Api
import com.tapin.tapin.network.Service

class App : Application() {
    // Get The API interface for all the apis
    val api: Api by lazy { Service().getClient().create(Api::class.java) }

    var profile: ProfileResponse? = null

    var cardDetailsResponse: CardDetailsResponse? = null

    override fun onCreate() {
        super.onCreate()
    }
}