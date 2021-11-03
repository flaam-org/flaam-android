package com.minor_project.flaamandroid.ui.userprofile.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyImplementationsViewModel @Inject constructor(private val repo: FlaamRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile

    private val _getImplementations = MutableLiveData<ApiResponse<ImplementationsResponse>>()
    val getImplementations: LiveData<ApiResponse<ImplementationsResponse>> = _getImplementations

    private val _voteImplementation = MutableLiveData<Int>()
    val voteImplementation: LiveData<Int> = _voteImplementation

    private val _deleteImplementation = MutableLiveData<Int>()
    val deleteImplementation: LiveData<Int> = _deleteImplementation

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }

    fun getImplementations(ownerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getImplementations(null, ownerId)
            _getImplementations.postValue(handleGetResponse(res))
        }
    }

    fun voteImplementation(id: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.voteImplementation(id, value)
            if (res.isSuccessful) {
                _voteImplementation.postValue(value)
            }

        }
    }

    fun deleteImplementation(id: Int) {
        viewModelScope.launch {
            val res = repo.deleteImplementation(id)
            if (res.isSuccessful) {
                _deleteImplementation.postValue(1)
            } else {
                _deleteImplementation.postValue(-1)
            }
        }
    }

}