package com.tapin.tapin.activity

import android.content.Intent
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tapin.tapin.R
import com.tapin.tapin.model.profile.CardDetailsResponse
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.utils.Debug
import com.tapin.tapin.utils.UrlGenerator
import com.tapin.tapin.utils.Utils
import cz.msebera.android.httpclient.Header

class SplashActivity : BaseActivity() {
    private var client: AsyncHttpClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()

        getProfile()
    }

    override fun onPause() {
        super.onPause()

        client?.cancelAllRequests(true)
    }

    private fun getProfile() {
        if (!showInternetNotWorking()) {
            return
        }

        val params = RequestParams().also {
            it.put("device_token", FirebaseInstanceId.getInstance().token ?: "")
            it.put("uuid", Utils.getDeviceID(this))
        }

        Debug.d("Okhttp", "API: " + UrlGenerator.getProfileApi() + " " + params.toString())

        client = AsyncHttpClient()
        client?.setTimeout(Constant.TIMEOUT)
        client?.post(UrlGenerator.getProfileApi(), params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    responseBody?.let {
                        val content = String(responseBody, Charsets.UTF_8)
                        Debug.d("Okhttp", "Success Response: $content")
                        val profileResponse = Gson().fromJson(content, ProfileResponse::class.java)

                        Debug.d("Hello", profileResponse.toString())

                        if (profileResponse.isValidProfile()) {
                            getConsumerCardDetails(profileResponse)
                        } else {
                            navigateToOrderScreen()
                        }
                    }
                } catch (e: Exception) {
                    navigateToOrderScreen()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                responseBody?.let {
                    val content = String(responseBody, Charsets.UTF_8)
                    Debug.d("Okhttp", "Failure Response: $content")
                }
                Debug.d("Hello", error?.message ?: "")
            }

            override fun onCancel() {
                super.onCancel()

                finish()
            }
        })
    }

    private fun getConsumerCardDetails(profileResponse: ProfileResponse) {
        if (!showInternetNotWorking()) {
            return
        }
        Debug.d("Okhttp", "API: " + UrlGenerator.getConsumerCardDetails(profileResponse.uid))

        client?.get(
            UrlGenerator.getConsumerCardDetails(profileResponse.uid),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        responseBody?.let {
                            val content = String(responseBody, Charsets.UTF_8)
                            Debug.d("Okhttp", "Success Response: $content")
                            val cardDetailsResponse =
                                Gson().fromJson(content, CardDetailsResponse::class.java)

                            Debug.d("Hello", cardDetailsResponse.toString())
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    responseBody?.let {
                        val content = String(responseBody, Charsets.UTF_8)
                        Debug.d("Okhttp", "Failure Response: $content")
                    }
                    Debug.d("Hello", error?.message ?: "")
                }

                override fun onCancel() {
                    super.onCancel()

                    finish()
                }

                override fun onFinish() {
                    super.onFinish()

                    navigateToOrderScreen()
                }
            })
    }

    private fun navigateToOrderScreen() {
        startActivity(Intent(this@SplashActivity, OrderSelectionActivity::class.java))
        finish()
    }
}