package com.minor_project.flaamandroid.ui.feed.post.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDescriptionViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {

    private val _getIdeaDetails = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val ideaDetails: LiveData<ApiResponse<IdeasResponse.Result>> = _getIdeaDetails

    fun getIdeaDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeaDetails(id)
            _getIdeaDetails.postValue(handleGetResponse(res))
        }
    }
}