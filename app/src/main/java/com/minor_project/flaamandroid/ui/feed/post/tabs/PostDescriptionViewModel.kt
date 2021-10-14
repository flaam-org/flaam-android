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
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostDescriptionViewModel @Inject constructor(private val repo: FlaamRepository) :
    ViewModel() {

    private val _getIdeaDetails = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val ideaDetails: LiveData<ApiResponse<IdeasResponse.Result>> = _getIdeaDetails

    private val _upvoteIdea = MutableLiveData<Response<Unit>>()
    val upvoteIdea: LiveData<Response<Unit>> = _upvoteIdea

    private val _downvoteIdea = MutableLiveData<Response<Unit>>()
    val downvoteIdea: LiveData<Response<Unit>> = _downvoteIdea

    fun getIdeaDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeaDetails(id)
            _getIdeaDetails.postValue(handleGetResponse(res))
        }
    }

    fun upvoteIdea(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.upvoteIdea(id)
            _upvoteIdea.postValue(res)
        }
    }

    fun downvoteIdea(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.downvoteIdea(id)
            _downvoteIdea.postValue(res)
        }
    }


}