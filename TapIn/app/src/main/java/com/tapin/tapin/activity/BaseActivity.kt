package com.tapin.tapin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tapin.tapin.model.OrderedInfo
import com.tapin.tapin.model.UserInfo
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.utils.PreferenceManager
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
}
