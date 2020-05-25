package com.tapin.tapin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tapin.tapin.network.Api
import java.lang.IllegalArgumentException

class CardDetailsViewModel(val api: Api) : ViewModel() {

}

@Suppress("UNCHECKED_CAST")
class CardDetailsViewModelFactory(private val api: Api) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailsViewModel::class.java)) {
            return CardDetailsViewModel(api) as T
        }

        throw IllegalArgumentException("Please make sure to pass the proper parameters to create this view model")
    }

}