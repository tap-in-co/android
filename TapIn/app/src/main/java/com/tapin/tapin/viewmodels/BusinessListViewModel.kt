package com.tapin.tapin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tapin.tapin.model.business.AllBusiness
import com.tapin.tapin.model.business.Business
import com.tapin.tapin.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BusinessListViewModel(val api: Api, private val ids: String) : ViewModel() {
    private val allBusinessMutableLiveData = MutableLiveData<Result<AllBusiness>>()
    val allBusinessLiveData: LiveData<Result<AllBusiness>> = allBusinessMutableLiveData

    init {
        getAllBusiness()
    }

    // getProfile And Card Details
    private fun getAllBusiness() {
        viewModelScope.launch(Dispatchers.Main) {
            kotlin.runCatching {
                return@runCatching api.getAllBusinessWithIds(ids = ids)
            }.onSuccess { allBusiness ->
                val filtered = allBusiness.data.filter { it.name != null && it.marketingStatement != null && it.address != null }
                allBusinessMutableLiveData.postValue(Result.success(AllBusiness(data = filtered, status = allBusiness.status)))
            }.onFailure {
                allBusinessMutableLiveData.postValue(Result.failure(it))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class BusinessListViewModelFactory(private val api: Api, private val ids: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessListViewModel::class.java)) {
            return BusinessListViewModel(api, ids) as T
        }

        throw IllegalArgumentException("Please make sure to pass the proper parameters to create this view model")
    }

}