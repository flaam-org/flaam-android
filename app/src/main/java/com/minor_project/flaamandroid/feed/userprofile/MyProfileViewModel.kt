package com.minor_project.flaamandroid.feed.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.TagsResponseItem
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(private val flaamRepo: FlaamRepository): ViewModel() {

    private val _userProfile = MutableLiveData<ApiException<ViewProfileResponse>>()
    val userProfile: LiveData<ApiException<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiException<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiException<UpdateProfileResponse>> = _updateUserProfile

    private val _tagsList = MutableLiveData<ApiException<TagsResponse>>()
    val tagsList: LiveData<ApiException<TagsResponse>> = _tagsList


    private val _tagsListFiltered = MutableLiveData<ApiException<TagsResponse>>()
    val tagsListFiltered: LiveData<ApiException<TagsResponse>> = _tagsListFiltered

    private val _tagsListFromIds = MutableLiveData<ApiException<TagsResponse>>()
    val tagsListFromIds: LiveData<ApiException<TagsResponse>> = _tagsListFromIds


    private val _createNewTag = MutableLiveData<ApiException<TagsResponseItem>>()
    val createNewTag: LiveData<ApiException<TagsResponseItem>> = _createNewTag






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
            Timber.e(idList.toString() +" | " + ids.toString())
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


}