package com.minor_project.flaamandroid.ui.fragments.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handleGetResponse
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: AuthRepository): ViewModel() {

    private val _loginResult = MutableLiveData<ApiException<LoginResponse>>()
    val loginResult: LiveData<ApiException<LoginResponse>> = _loginResult

    fun postLoginRequest(data: LoginRequest){
        viewModelScope.launch {
            val res = repo.postLogin(data)
            _loginResult.postValue(handleGetResponse(res))
        }
    }
}