package com.tapin.tapin.callbacks

import com.tapin.tapin.model.market.Market
import com.tapin.tapin.model.resturants.Business

interface Communication {
    fun onMarketSelected(market: Market)

    fun onProfileBackClicked()

    fun onProfileManageCardClicked()

    fun onBusinessSelected(business: Business, market: Market)
}