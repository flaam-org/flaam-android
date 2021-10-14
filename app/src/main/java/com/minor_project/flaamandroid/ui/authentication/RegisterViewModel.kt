package com.minor_project.flaamandroid.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {

    private val _registerUserResult = MutableLiveData<ApiResponse<RegisterLoginResponse>>()
    val registerUserResult: LiveData<ApiResponse<RegisterLoginResponse>> = _registerUserResult


    fun postRegisterUser(body: RegisterLoginRequest) {
        viewModelScope.launch {
            val res = repo.registerUser(body)
            _registerUserResult.postValue(handlePostResponse(res))
        }
    }


}