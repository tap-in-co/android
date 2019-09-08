package com.tapin.tapin.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.tapin.tapin.model.profile.GetProfileRequest
import com.tapin.tapin.network.Api
import com.tapin.tapin.utils.Debug
import com.tapin.tapin.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.UnknownHostException

class SplashViewModel(val api: Api) : ViewModel() {
    private val navigateMutableLiveData = MutableLiveData<Boolean>().also {
        it.postValue(false)
    }
    val navigateLiveData: LiveData<Boolean> = navigateMutableLiveData

    private val errorMutableLiveData = MutableLiveData<String?>().also {
        it.postValue(null)
    }
    val errorLiveData: LiveData<String?> = errorMutableLiveData

    override fun onCleared() {
        super.onCleared()

        Debug.d("SplashViewModel", "onCleared() called.")
    }

    // getProfile And Card Details
    fun getProfile(deviceToken: String, uuid: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val deferredProfile = viewModelScope.async(Dispatchers.IO) {
                api.getProfile(
                    GetProfileRequest(
                        deviceToken = deviceToken,
                        uuid = uuid
                    )
                )
            }

            try {
                val profile = deferredProfile.await()
                Debug.d("SplashViewModel", "Profile : $profile")

                if (profile.isValidProfile()) {
                    val deferredCardDetails = viewModelScope.async(Dispatchers.IO) {
                        api.getCardDetails(id = profile.uid)
                    }

                    val cardDetails = deferredCardDetails.await()
                    Debug.d("SplashViewModel", "Card Details : $cardDetails")
                }

                askToNavigate()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> {
                        errorMutableLiveData.postValue("We can not proceed without network, please check your network connection.")
                    }
                    else -> askToNavigate()
                }
                Debug.d("SplashViewModel", "$e")
                Debug.d("SplashViewModel", "${e.message}")
            }
        }
    }

    private fun askToNavigate() {
        navigateMutableLiveData.postValue(true)
    }
}

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(val api: Api) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(api) as T
        }

        throw IllegalArgumentException("Please make sure to pass the proper parameters to create this view model")
    }

}

object SplashViewModelHelper {
    fun provideSplashViewModelFactory(context: Context): SplashViewModelFactory {
        val preferenceManager: PreferenceManager = context.applicationContext as PreferenceManager

        return SplashViewModelFactory(preferenceManager.api)
    }
}