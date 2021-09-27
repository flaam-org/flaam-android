package com.minor_project.flaamandroid.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.GetIdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {

    private val _tags = MutableLiveData<ApiResponse<TagsResponse>>()
    val tags: LiveData<ApiResponse<TagsResponse>> = _tags

    private val _getIdeas = MutableLiveData<ApiResponse<GetIdeasResponse>>()
    val getIdeas: LiveData<ApiResponse<GetIdeasResponse>> = _getIdeas


    fun getTags() {
        viewModelScope.launch {
            val res = repo.getTagsList()
            _tags.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getTagsForKeyword(keyword, null)
            _tags.postValue(handleGetResponse(res))
        }
    }

    fun getIdeas() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeas()
            _getIdeas.postValue(handleGetResponse(res))
        }
    }


}