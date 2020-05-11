package com.tapin.tapin.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.tapin.tapin.viewmodels.SplashViewModel
import com.tapin.tapin.viewmodels.SplashViewModelHelper
import cz.msebera.android.httpclient.Header

class SplashActivity : BaseActivity() {
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModelAndStartObserving()
    }

    private fun setupViewModelAndStartObserving() {
        val factory = SplashViewModelHelper.provideSplashViewModelFactory(this)
        splashViewModel = ViewModelProvider(viewModelStore, factory).get(SplashViewModel::class.java)

        // Start Observing Live Data/ Datum
        splashViewModel.navigateLiveData.observe(this, Observer {
            it?.let {
                if (it) {
                    navigateToOrderScreen()
                }
            }
        })

        splashViewModel.errorLiveData.observe(this, Observer {
            it?.let {
                Toast.makeText(this@SplashActivity, it, Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        // Ask View Model to lod the required data for the current data
        splashViewModel.getProfile(FirebaseInstanceId.getInstance().token ?: "", Utils.getDeviceID(this))
    }

    private fun navigateToOrderScreen() {
        startActivity(Intent(this, OrderSelectionActivity::class.java))
        finish()
    }
}