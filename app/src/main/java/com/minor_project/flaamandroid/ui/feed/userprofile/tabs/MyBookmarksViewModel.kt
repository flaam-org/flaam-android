package com.minor_project.flaamandroid.ui.feed.userprofile.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookmarksViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiResponse<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiResponse<UpdateProfileResponse>> = _updateUserProfile



    fun getUserProfile()
    {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun updateUserProfile(data: UpdateProfileRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.updateUserProfile(data)
            _updateUserProfile.postValue(handleGetResponse(res))
        }
    }
}