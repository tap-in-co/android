package com.tapin.tapin.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tapin.tapin.R
import com.tapin.tapin.model.OrderedInfo
import com.tapin.tapin.model.UserInfo
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.utils.PreferenceManager
import com.tapin.tapin.utils.Utils
import java.util.*

/**
 * Created by Narendra on 4/27/17.
 */

open class BaseActivity : AppCompatActivity() {

    var listOrdered = ArrayList<OrderedInfo>()
    var isCorporateOrder: Boolean = false
    var corporateOrderMerchantIds: String = ""
    var userInfo: UserInfo? = null

    companion object {

        // This Boolean indicated, whether this is a New Order or Pickup Oder
        var isPickUpOrder = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCorporateOrder = PreferenceManager.getInstance().isCorporateOrder
        corporateOrderMerchantIds = PreferenceManager.getInstance().corporateOrderMerchantIds
        userInfo = PreferenceManager.getUserInfo()
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
}
