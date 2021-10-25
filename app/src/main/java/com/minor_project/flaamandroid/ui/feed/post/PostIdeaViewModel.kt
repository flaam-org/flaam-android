package com.minor_project.flaamandroid.ui.feed.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.CreateUpdateIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostIdeaViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _postIdea = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val postIdea: LiveData<ApiResponse<IdeasResponse.Result>> = _postIdea


    private val _finalTagsList = MutableLiveData<List<Int>>()
    val finalTagsList: LiveData<List<Int>> = _finalTagsList


    private val _tagsList = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsList: LiveData<ApiResponse<TagsResponse>> = _tagsList


    private val _tagsListFiltered = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFiltered: LiveData<ApiResponse<TagsResponse>> = _tagsListFiltered


    private val _createNewTag = MutableLiveData<ApiResponse<TagsResponse.Result>>()
    val createNewTag: LiveData<ApiResponse<TagsResponse.Result>> = _createNewTag

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }


    fun postIdea(body: CreateUpdateIdeaRequest) {
        viewModelScope.launch {
            val res = flaamRepo.postIdea(body)
            _postIdea.postValue(handlePostResponse(res))
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

    fun finalTagsList(list: ArrayList<Int>) {
        _finalTagsList.value = list
    }

}