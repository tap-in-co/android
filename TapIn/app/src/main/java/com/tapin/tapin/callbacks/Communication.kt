package com.tapin.tapin.callbacks

import com.tapin.tapin.model.market.Market

interface Communication {
    fun onMarketSelected(market: Market)
}