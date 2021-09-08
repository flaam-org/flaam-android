package com.minor_project.flaamandroid.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repo: FlaamRepository): ViewModel() {

    private val _tags = MutableLiveData<ApiException<TagsResponse>>()
    val tags: LiveData<ApiException<TagsResponse>> = _tags


    fun getTags() {
        viewModelScope.launch {
            val res = repo.getTagsList()
            _tags.postValue(handleGetResponse(res))
        }
    }

    fun getTagsForKeyword(keyword: String){
        viewModelScope.launch {
            val res = repo.getTagsForKeyword(keyword)
            _tags.postValue(handleGetResponse(res))
        }
    }


}