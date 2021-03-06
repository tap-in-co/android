package com.tapin.tapin.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tapin.tapin.model.UserInfo
import com.tapin.tapin.utils.PreferenceManager

open class BaseFragment : Fragment() {
    var isCorporateOrder: Boolean = false
    var corporateOrderMerchantIds: String = ""
    var userInfo: UserInfo? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isCorporateOrder = PreferenceManager.getInstance().isCorporateOrder
        corporateOrderMerchantIds = PreferenceManager.getInstance().corporateOrderMerchantIds
        userInfo = PreferenceManager.getUserInfo()
    }
}