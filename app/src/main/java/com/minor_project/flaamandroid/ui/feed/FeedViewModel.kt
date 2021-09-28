package com.minor_project.flaamandroid.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {

    private val _tags = MutableLiveData<ApiResponse<TagsResponse>>()
    val tags: LiveData<ApiResponse<TagsResponse>> = _tags

    private val _tagsListFromIds = MutableLiveData<ApiResponse<TagsResponse>>()
    val tagsListFromIds: LiveData<ApiResponse<TagsResponse>> = _tagsListFromIds

    private val _getIdeas = MutableLiveData<ApiResponse<IdeasResponse>>()
    val ideas: LiveData<ApiResponse<IdeasResponse>> = _getIdeas


    fun getTags() {
        viewModelScope.launch {
            val res = repo.getTagsList()
            _tags.postValue(handleGetResponse(res))
        }
    }


    fun getTagsFromIds(idList : List<Int>?){
        viewModelScope.launch(Dispatchers.IO) {
            val ids = idList.toString().substring(1, idList.toString().length - 1)
            Timber.e(idList.toString() +" | " + ids.toString())
            val res = repo.getTagsForKeyword(null, ids)
            _tagsListFromIds.postValue(handleGetResponse(res))
        }
    }


    fun getTagsForKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getTagsForKeyword(keyword, null)
            _tags.postValue(handleGetResponse(res))
        }
    }

    fun getIdeas(offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeas(5, offset)
            _getIdeas.postValue(handleGetResponse(res))
        }
    }


}