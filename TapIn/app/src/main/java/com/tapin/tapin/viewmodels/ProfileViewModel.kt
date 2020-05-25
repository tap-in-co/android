package com.tapin.tapin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tapin.tapin.model.profile.ProfileRequest
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ProfileViewModel(val api: Api) : ViewModel() {
    private val profileMutableLiveData = MutableLiveData<Result<ProfileResponse>>()
    val profileLiveData: LiveData<Result<ProfileResponse>> = profileMutableLiveData

    private val loadingMutableLiveData = MutableLiveData<Boolean>().also { false }
    val loadingLiveData: LiveData<Boolean> = loadingMutableLiveData

    // getProfile And Card Details
    fun saveProfile(profileRequest: ProfileRequest) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            kotlin.runCatching {
                return@runCatching api.createProfile(profileRequest)
            }.onSuccess { profileResponse ->
                loadingMutableLiveData.postValue(false)
                //val filtered = allBusiness.data.filter { it.name != null && it.marketingStatement != null && it.address != null }
                profileMutableLiveData.postValue(Result.success(profileResponse))
            }.onFailure {
                loadingMutableLiveData.postValue(false)
                profileMutableLiveData.postValue(Result.failure(it))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val api: Api) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(api) as T
        }

        throw IllegalArgumentException("Please make sure to pass the proper parameters to create this view model")
    }

}
