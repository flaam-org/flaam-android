package com.minor_project.flaamandroid.ui.feed.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiResponse<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiResponse<UpdateProfileResponse>> = _updateUserProfile

    private val _tagsListFromIds = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFromIds: LiveData<ApiResponse<TagsResponse>> = _tagsListFromIds


    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun updateUserProfile(data: UpdateProfileRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.updateUserProfile(data)
            _updateUserProfile.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForId(idList: List<Int>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val ids = idList.toString().substring(1, idList.toString().length - 1)
            Timber.e(idList.toString() + " | " + ids.toString())
            val res = flaamRepo.getTagsForKeyword(null, ids)
            _tagsListFromIds.postValue(handleGetResponse(res))
        }
    }

}