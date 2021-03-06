package com.minor_project.flaamandroid.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
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

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _updateUserProfile = MutableLiveData<ApiResponse<UpdateProfileResponse>>()
    val updateUserProfile: LiveData<ApiResponse<UpdateProfileResponse>> = _updateUserProfile

    private val _addIdeaToUsersBookmarks = MutableLiveData<Response<Unit>>()
    val addIdeaToUsersBookmarks: LiveData<Response<Unit>> = _addIdeaToUsersBookmarks

    private val _removeIdeaFromUsersBookmarks = MutableLiveData<Response<Unit>>()
    val removeIdeaFromUsersBookmarks: LiveData<Response<Unit>> = _removeIdeaFromUsersBookmarks


    fun resetIdeas(){
        _getIdeas.value = ApiResponse.Loading()
    }

    fun getTags() {
        viewModelScope.launch {
            val res = repo.getTagsList()
            _tags.postValue(handleGetResponse(res))
        }
    }


    fun getTagsFromIds(idList: List<Int>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val ids = idList.toString().substring(1, idList.toString().length - 1)
            Timber.e(idList.toString() + " | " + ids.toString())
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

    fun getIdeas(offset: Int, ordering: String? = null, tags: ArrayList<Int>? = null, search: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val _tags = if(tags?.isNotEmpty() == true) tags.joinToString{","} else null
            Timber.e(_tags + "viewm")
            val res = repo.getIdeas(5, offset, null, null, ordering, _tags, search)
            _getIdeas.postValue(handleGetResponse(res))
        }
    }

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun updateUserProfile(data: UpdateProfileRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.updateUserProfile(data)
            _updateUserProfile.postValue(handleGetResponse(res))
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