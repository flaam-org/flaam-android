package com.minor_project.flaamandroid.ui.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _userFavouriteTagsList = MutableLiveData<ApiResponse<TagsResponse>>()
    val userFavouriteTagsList: LiveData<ApiResponse<TagsResponse>> = _userFavouriteTagsList

    fun getUserProfileFromUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserProfileFromUsername(username)
            _userProfile.postValue(handleGetResponse(res))
        }
    }


    fun getUserFavouriteTags(favouritedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserFavouriteTags(favouritedBy)
            _userFavouriteTagsList.postValue(handleGetResponse(res))
        }
    }
}