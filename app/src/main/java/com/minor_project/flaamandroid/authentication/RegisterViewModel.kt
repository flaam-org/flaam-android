package com.minor_project.flaamandroid.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.RegisterUserResponse
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.network.FlaamRepository
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.handlePostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repo: AuthRepository): ViewModel() {

    private val _registerUserResult = MutableLiveData<ApiException<RegisterUserResponse>>()
    val registerUserResult: LiveData<ApiException<RegisterUserResponse>> = _registerUserResult


    fun postRegisterUser(body: LoginRequest){
        viewModelScope.launch {
            val res = repo.registerUser(body)
            _registerUserResult.postValue(handlePostResponse(res))
        }
    }



}