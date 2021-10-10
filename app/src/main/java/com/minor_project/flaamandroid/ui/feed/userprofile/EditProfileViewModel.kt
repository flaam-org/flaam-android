package com.minor_project.flaamandroid.ui.feed.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiResponse<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiResponse<UpdateProfileResponse>> = _updateUserProfile

    private val _tagsList = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsList: LiveData<ApiResponse<TagsResponse>> = _tagsList


    private val _tagsListFiltered = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFiltered: LiveData<ApiResponse<TagsResponse>> = _tagsListFiltered


    private val _createNewTag = MutableLiveData<ApiResponse<TagsResponse.Result>>()
    val createNewTag: LiveData<ApiResponse<TagsResponse.Result>> = _createNewTag


    private val _userFavouriteTagsList = MutableLiveData<ApiResponse<TagsResponse>>()
    val userFavouriteTagsList: LiveData<ApiResponse<TagsResponse>> = _userFavouriteTagsList


    private val _addTagToUsersFavouriteTags = MutableLiveData<Response<Unit>>()
    val addTagToUsersFavouriteTags: LiveData<Response<Unit>> = _addTagToUsersFavouriteTags

    private val _removeTagFromUsersFavouriteTags = MutableLiveData<Response<Unit>>()
    val removeTagFromUsersFavouriteTags: LiveData<Response<Unit>> = _removeTagFromUsersFavouriteTags

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


    fun getTags() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getTagsList()
            _tagsList.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForKeyword(keyword: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getTagsForKeyword(keyword, null)
            _tagsListFiltered.postValue(handleGetResponse(res))
        }
    }


    fun createNewTag(body: TagsRequest) {
        viewModelScope.launch {
            val res = flaamRepo.createNewTag(body)
            _createNewTag.postValue(handlePostResponse(res))
        }
    }

    fun getUserFavouriteTags(favouritedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserFavouriteTags(favouritedBy)
            _userFavouriteTagsList.postValue(handleGetResponse(res))
        }
    }


    fun addTagToUsersFavouriteTags(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.addTagToUsersFavouriteTags(id)
            _addTagToUsersFavouriteTags.postValue(res)
        }
    }

    fun removeTagFromUsersFavouriteTags(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.removeTagFromUsersFavouriteTags(id)
            _removeTagFromUsersFavouriteTags.postValue(res)
        }
    }


}