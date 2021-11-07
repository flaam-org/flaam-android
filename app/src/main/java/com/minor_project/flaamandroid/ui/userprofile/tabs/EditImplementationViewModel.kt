package com.minor_project.flaamandroid.ui.userprofile.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.AddUpdateImplementationRequest
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditImplementationViewModel @Inject constructor(private val repo: FlaamRepository) :
    ViewModel() {

    private val _updateImplementation =
        MutableLiveData<ApiResponse<ImplementationsResponse.Result>>()
    val updateImplementation: LiveData<ApiResponse<ImplementationsResponse.Result>> =
        _updateImplementation

    private val _getImplementationDetails =
        MutableLiveData<ApiResponse<ImplementationsResponse.Result>>()
    val getImplementationDetails: LiveData<ApiResponse<ImplementationsResponse.Result>> =
        _getImplementationDetails

    fun getImplementationDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getImplementationDetails(id)
            _getImplementationDetails.postValue(handleGetResponse(res))
        }
    }


    fun updateImplementation(id: Int, body: AddUpdateImplementationRequest) {
        viewModelScope.launch {
            val res = repo.updateImplementation(id, body)
            _updateImplementation.postValue(handleGetResponse(res))
        }
    }
}