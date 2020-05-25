package com.tapin.tapin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tapin.tapin.model.GetPointsResp
import com.tapin.tapin.model.profile.CardDetailsResponse
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.network.Api
import com.tapin.tapin.network.Service
import com.tapin.tapin.utils.PreferenceManager
import com.tapin.tapin.utils.Utils

class App : Application() {
    // Get The API interface for all the apis
    val api: Api by lazy { Service().getClient().create(Api::class.java) }

    var profile: ProfileResponse? = null

    var cardDetailsResponse: CardDetailsResponse? = null

    override fun onCreate() {
        super.onCreate()
    }

    fun putPointsData(getPointsResp: GetPointsResp?) {
        val sharedPreference: SharedPreferences = getSharedPreferences("points", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        if (getPointsResp != null) {
            editor.putString(
                "PointsData",
                Gson().toJson(getPointsResp)
            )
            editor.commit()
        }
    }

    fun getPointsData(): GetPointsResp? {
        val sharedPreference: SharedPreferences = getSharedPreferences("points", Context.MODE_PRIVATE)

        var userInfo: GetPointsResp? = null
        val content =
            sharedPreference.getString("PointsData", "")
        if (Utils.isEmpty(content)) return userInfo
        try {
            val type = object : TypeToken<GetPointsResp?>() {}.type
            userInfo = Gson().fromJson(
                content,
                type
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return userInfo
    }
}