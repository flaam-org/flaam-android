package com.minor_project.flaamandroid.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.response.PostIdeaResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostIdeaViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _postIdea = MutableLiveData<ApiResponse<PostIdeaResponse>>()
    val postIdea: LiveData<ApiResponse<PostIdeaResponse>> = _postIdea


    fun postIdea(body: PostIdeaRequest) {
        viewModelScope.launch {
            val res = flaamRepo.postIdea(body)
            _postIdea.postValue(handlePostResponse(res))
        }
    }

}