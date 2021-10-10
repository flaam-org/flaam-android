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

    private val _tagsListFromIds = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFromIds: LiveData<ApiResponse<TagsResponse>> = _tagsListFromIds


    private val _createNewTag = MutableLiveData<ApiResponse<TagsResponse.Result>>()
    val createNewTag: LiveData<ApiResponse<TagsResponse.Result>> = _createNewTag


    private val _addTagToUsersFavouriteTags = MutableLiveData<ApiResponse<Unit>>()
    val addTagToUsersFavouriteTags: LiveData<ApiResponse<Unit>> = _addTagToUsersFavouriteTags

    private val _removeTagFromUsersFavouriteTags = MutableLiveData<ApiResponse<Unit>>()
    val removeTagFromUsersFavouriteTags: LiveData<ApiResponse<Unit>> = _removeTagFromUsersFavouriteTags

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


    fun getTags() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getTagsList()
            _tagsList.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForKeyword(keyword: String?){
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getTagsForKeyword(keyword, null)
            _tagsListFiltered.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForId(idList : List<Int>?){


        viewModelScope.launch(Dispatchers.IO) {
            val ids = idList.toString().substring(1, idList.toString().length - 1)
            Timber.e(idList.toString() +" | " + ids)
            val res = flaamRepo.getTagsForKeyword(null, ids)
            _tagsListFromIds.postValue(handleGetResponse(res))
        }
    }

    fun createNewTag(body: TagsRequest){
        viewModelScope.launch {
            val res = flaamRepo.createNewTag(body)
            _createNewTag.postValue(handlePostResponse(res))
        }
    }


    fun addTagToUsersFavouriteTags(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.addTagToUsersFavouriteTags(id)
            _addTagToUsersFavouriteTags.postValue(handleGetResponse(res))
        }
    }

    fun removeTagFromUsersFavouriteTags(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.removeTagFromUsersFavouriteTags(id)
            _removeTagFromUsersFavouriteTags.postValue(handlePostResponse(res))
        }
    }

}