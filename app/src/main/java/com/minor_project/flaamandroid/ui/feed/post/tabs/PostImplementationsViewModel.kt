package com.minor_project.flaamandroid.ui.feed.post.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostImplementationsViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {


    private val _getImplementations = MutableLiveData<ApiResponse<ImplementationsResponse>>()
    val getImplementations: LiveData<ApiResponse<ImplementationsResponse>> = _getImplementations

    private val _voteImplementation = MutableLiveData<Int>()
    val voteImplementation: LiveData<Int> = _voteImplementation


    fun getImplementations(ideaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getImplementations(ideaId, null)
            _getImplementations.postValue(handleGetResponse(res))
        }
    }

    fun voteImplementation(id: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.voteImplementation(id, value)
            if (res.isSuccessful) {
                _voteImplementation.postValue(value)
            }

        }
    }


}