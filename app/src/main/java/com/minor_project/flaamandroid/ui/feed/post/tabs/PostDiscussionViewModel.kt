package com.minor_project.flaamandroid.ui.feed.post.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.CreateDiscussionRequest
import com.minor_project.flaamandroid.data.request.PostCommentRequest
import com.minor_project.flaamandroid.data.response.CommentsForDiscussionResponse
import com.minor_project.flaamandroid.data.response.CreateDiscussionResponse
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostDiscussionViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _createDiscussion = MutableLiveData<ApiResponse<CreateDiscussionResponse>>()
    val createDiscussion: LiveData<ApiResponse<CreateDiscussionResponse>> = _createDiscussion

    private val _getDiscussions = MutableLiveData<ApiResponse<DiscussionsResponse>>()
    val getDiscussions: LiveData<ApiResponse<DiscussionsResponse>> = _getDiscussions

    private val _voteDiscussion = MutableLiveData<Int>()
    val voteDiscussion: LiveData<Int> = _voteDiscussion


    private val _comments = MutableLiveData<ApiResponse<CommentsForDiscussionResponse>>()
    val comments: LiveData<ApiResponse<CommentsForDiscussionResponse>> = _comments

    private val _postCommentResult = MutableLiveData<ApiResponse<Any>>()
    val postCommentResult: LiveData<ApiResponse<Any>> = _postCommentResult

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }


    fun createDiscussion(body: CreateDiscussionRequest) {
        viewModelScope.launch {
            val res = flaamRepo.createDiscussion(body)
            _createDiscussion.postValue(handlePostResponse(res))
        }
    }

    fun getDiscussions(ideaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getDiscussions(ideaId)
            _getDiscussions.postValue(handleGetResponse(res))
        }
    }

    fun voteDiscussion(id: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.voteDiscussion(id, value)
            if(res.isSuccessful){
                _voteDiscussion.postValue(value)
            }

        }
    }

    fun getComments(id: Int){
        viewModelScope.launch {
            val res = flaamRepo.getCommentsForDiscussion(id)
            _comments.postValue(handleGetResponse(res))
        }
    }

    fun postComment(body: PostCommentRequest){
        viewModelScope.launch {
            val res = flaamRepo.postCommentForDiscussion(body)
            _postCommentResult.postValue(handlePostResponse(res))
        }
    }
}