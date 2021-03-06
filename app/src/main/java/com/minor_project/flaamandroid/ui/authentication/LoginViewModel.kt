package com.minor_project.flaamandroid.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val flaamRepo: FlaamRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<ApiResponse<RegisterLoginResponse>>()
    val registerLoginResult: LiveData<ApiResponse<RegisterLoginResponse>> = _loginResult

    private val _userProfile = MutableLiveData<ApiResponse<ViewProfileResponse>>()
    val userProfile: LiveData<ApiResponse<ViewProfileResponse>> = _userProfile


    fun postLoginRequest(data: RegisterLoginRequest) {
        viewModelScope.launch {
            val res = repo.postLogin(data)
            _loginResult.postValue(handleGetResponse(res))
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            val res = flaamRepo.getUserProfile()
            _userProfile.postValue(handleGetResponse(res))
        }
    }
}