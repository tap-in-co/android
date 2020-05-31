package com.tapin.tapin.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.tapin.tapin.App
import com.tapin.tapin.R
import com.tapin.tapin.model.profile.CardDetailsResponse
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.utils.Utils
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.SplashViewModel

class SplashActivity : BaseActivity() {
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModelAndStartObserving()
    }

    private fun setupViewModelAndStartObserving() {
        val factory = AppViewModelProvider.provideSplashViewModelFactory(this)
        splashViewModel = ViewModelProvider(viewModelStore, factory).get(SplashViewModel::class.java)

        // Start Observing Live Data/ Datum
        splashViewModel.navigateLiveData.observe(this, Observer {
            it?.let {
                saveProfileAndCardInfoInMemory(it.second, it.third)
                if (it.first) {
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

    private fun saveProfileAndCardInfoInMemory(profileResponse: ProfileResponse?, cardDetailsResponse: CardDetailsResponse?) {
        val app: App = applicationContext as App
        app.profile = profileResponse
        app.cardDetailsResponse = cardDetailsResponse
    }

    private fun navigateToOrderScreen() {
        if (checkLocationPermission()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            showAllowLocationDialog()
            //finish()
        }
    }

    private fun showAllowLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Grant Location to proceed")
            .setMessage("Go to App Settings and grand location permission")
            .setPositiveButton(
                "Ok"
            ) { dialogInterface, _ -> //Prompt the user once explanation has been shown
                dialogInterface.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    it.data = uri
                }
                startActivity(intent)

                this.finish()
            }
            .create()
            .show()
    }
}