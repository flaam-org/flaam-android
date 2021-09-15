package com.minor_project.flaamandroid.feed.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository): ViewModel() {

    private val _userProfile = MutableLiveData<ApiException<ViewProfileResponse>>()
    val userProfile: LiveData<ApiException<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiException<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiException<UpdateProfileResponse>> = _updateUserProfile


    fun getUserProfile()
    {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun updateUserProfile(data : UpdateProfileRequest)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.updateUserProfile(data)
            _updateUserProfile.postValue(handleGetResponse(res))
        }
    }

}