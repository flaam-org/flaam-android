package com.minor_project.flaamandroid.ui.userprofile.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyBookmarksViewModel @Inject constructor(private val repo: FlaamRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _getIdeas = MutableLiveData<ApiResponse<IdeasResponse>>()
    val ideas: LiveData<ApiResponse<IdeasResponse>> = _getIdeas

    private val _addIdeaToUsersBookmarks = MutableLiveData<Response<Unit>>()
    val addIdeaToUsersBookmarks: LiveData<Response<Unit>> = _addIdeaToUsersBookmarks

    private val _removeIdeaFromUsersBookmarks = MutableLiveData<Response<Unit>>()
    val removeIdeaFromUsersBookmarks: LiveData<Response<Unit>> = _removeIdeaFromUsersBookmarks


    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun getIdeas(bookmarkedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getIdeas(null, 0, null, bookmarkedBy)
            _getIdeas.postValue(handleGetResponse(res))
        }
    }

    fun addIdeaToUsersBookmarks(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.addIdeaToUsersBookmarks(id)
            _addIdeaToUsersBookmarks.postValue(res)
        }
    }

    fun removeIdeaFromUsersBookmarks(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.removeIdeaFromUsersBookmarks(id)
            _removeIdeaFromUsersBookmarks.postValue(res)
        }
    }
}