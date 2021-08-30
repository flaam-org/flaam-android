package com.minor_project.flaamandroid.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository): ViewModel() {

    private val _userProfile = MutableLiveData<ApiException<ViewProfileResponse>>()
    val userProfile: LiveData<ApiException<ViewProfileResponse>> = _userProfile

    fun getUserProfile(){
        viewModelScope.launch {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }
}