package com.tapin.tapin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tapin.tapin.model.market.AllMarkets
import com.tapin.tapin.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MarketListViewModel(val api: Api) : ViewModel() {
    private val allMarketMutableLiveData = MutableLiveData<Result<AllMarkets>>()
    val allMarketLiveData: LiveData<Result<AllMarkets>> = allMarketMutableLiveData

    init {
        getAllMarkets()
    }

    // getProfile And Card Details
    private fun getAllMarkets() {
        viewModelScope.launch(Dispatchers.Main) {
            kotlin.runCatching {
                return@runCatching api.getAllMarkets()
            }.onSuccess {
                allMarketMutableLiveData.postValue(Result.success(it))
            }.onFailure {
                allMarketMutableLiveData.postValue(Result.failure(it))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MarketViewModelFactory(private val api: Api) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketListViewModel::class.java)) {
            return MarketListViewModel(api) as T
        }

        throw IllegalArgumentException("Please make sure to pass the proper parameters to create this view model")
    }

}