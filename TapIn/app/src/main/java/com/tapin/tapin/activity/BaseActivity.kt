package com.tapin.tapin.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tapin.tapin.R
import com.tapin.tapin.model.OrderedInfo
import com.tapin.tapin.model.UserInfo
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.utils.Utils
import java.util.ArrayList

/**
 * Created by Narendra on 4/27/17.
 */

private const val MY_PERMISSIONS_REQUEST_LOCATION: Int = 99

open class BaseActivity : AppCompatActivity(), LocationListener {

    var listOrdered = ArrayList<OrderedInfo>()
    var isCorporateOrder: Boolean = false
    var corporateOrderMerchantIds: String = ""
    var userInfo: UserInfo? = null

    private lateinit var locationManager: LocationManager

    var location: Location? = null

    companion object {

        // This Boolean indicated, whether this is a New Order or Pickup Oder
        var isPickUpOrder = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            //Request location updates:
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000 * 60 * 1.toLong(), // 1 minute
                10.toFloat(), //// 10 meters
                this
            )

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000 * 60 * 1.toLong(), // 1 minute
                10.toFloat(), //// 10 meters
                this
            )
        }

        //isCorporateOrder = PreferenceManager.getInstance().isCorporateOrder
        //corporateOrderMerchantIds = PreferenceManager.getInstance().corporateOrderMerchantIds
        //userInfo = PreferenceManager.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.removeUpdates(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        //Request location updates:
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000 * 60 * 1.toLong(), // 1 minute
                            10.toFloat(), //// 10 meters
                            this
                        )
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    // Removes the list
    fun clearOrders() {
        Constant.listOrdered.clear()
    }

    fun showInternetNotWorking(): Boolean {
        if (!Utils.isInternetConnected(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.alert_check_connection))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    finish()
                }

            val alert = builder.create()
            alert.show()

            return false
        }

        return true
    }

    fun showUnableToOrderDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Sorry!")
        builder.setMessage(R.string.join_tap_in_text_with_email)
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ -> }

        val alert = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Grant Location to proceed")
                    .setMessage("Go to App Settings and grand location permission")
                    .setPositiveButton(
                        "Ok"
                    ) { _, _ -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}
