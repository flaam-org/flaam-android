package com.minor_project.flaamandroid.ui.feed.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.AddImplementationRequest
import com.minor_project.flaamandroid.data.response.AddImplementationResponse
import com.minor_project.flaamandroid.data.response.IdeasResponse
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
class AddImplementationViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {


    private val _addImplementation = MutableLiveData<ApiResponse<AddImplementationResponse>>()
    val addImplementation: LiveData<ApiResponse<AddImplementationResponse>> = _addImplementation

    private val _getIdeaDetails = MutableLiveData<ApiResponse<IdeasResponse.Result>>()
    val ideaDetails: LiveData<ApiResponse<IdeasResponse.Result>> = _getIdeaDetails


    fun addImplementation(body: AddImplementationRequest) {
        viewModelScope.launch {
            val res = flaamRepo.addImplementation(body)
            _addImplementation.postValue(handlePostResponse(res))
        }
    }

    fun getIdeaDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = flaamRepo.getIdeaDetails(id)
            _getIdeaDetails.postValue(handleGetResponse(res))
        }
    }

}