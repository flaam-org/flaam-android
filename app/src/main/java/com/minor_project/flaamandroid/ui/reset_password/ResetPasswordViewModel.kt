package com.minor_project.flaamandroid.ui.reset_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

//    private val _resetPassResult = MutableLiveData<>

    fun getResetPasswordToken(email: String){
        viewModelScope.launch {
            val res = authRepository.getResetPasswordToken(RegisterLoginRequest(null, null, null, null, email, null,null, null, null, null, null, null, null))
            postResetPassword("")
        }

    }


    suspend fun postResetPassword(token: String){

        val res = authRepository.postResetPassword(token)

    }



}