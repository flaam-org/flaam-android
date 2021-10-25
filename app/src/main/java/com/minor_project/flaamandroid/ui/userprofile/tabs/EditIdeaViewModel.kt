package com.minor_project.flaamandroid.ui.userprofile.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditIdeaViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {


    private val _updateIdea = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val updateIdea: LiveData<ApiResponse<IdeasResponse.Result>> = _updateIdea

    private val _getIdeaDetails = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val ideaDetails: LiveData<ApiResponse<IdeasResponse.Result>> = _getIdeaDetails


    private val _finalTagsList = MutableLiveData<List<Int>>()
    val finalTagsList: LiveData<List<Int>> = _finalTagsList


    private val _tagsList = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsList: LiveData<ApiResponse<TagsResponse>> = _tagsList


    private val _tagsListFiltered = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFiltered: LiveData<ApiResponse<TagsResponse>> = _tagsListFiltered


    private val _createNewTag = MutableLiveData<ApiResponse<TagsResponse.Result>>()
    val createNewTag: LiveData<ApiResponse<TagsResponse.Result>> = _createNewTag


    fun getTags() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getTagsList()
            _tagsList.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForKeyword(keyword: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getTagsForKeyword(keyword, null)
            _tagsListFiltered.postValue(handleGetResponse(res))
        }
    }


    fun createNewTag(body: TagsRequest) {
        viewModelScope.launch {
            val res = repo.createNewTag(body)
            _createNewTag.postValue(handlePostResponse(res))
        }
    }

    fun finalTagsList(list: ArrayList<Int>) {
        _finalTagsList.value = list
    }

    fun getIdeaDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeaDetails(id)
            _getIdeaDetails.postValue(handleGetResponse(res))
        }
    }


    fun updateIdea(body: PostIdeaRequest, id: Int) {
        viewModelScope.launch {
            val res = repo.updateIdea(body, id)
            _updateIdea.postValue(handleGetResponse(res))
        }
    }

}