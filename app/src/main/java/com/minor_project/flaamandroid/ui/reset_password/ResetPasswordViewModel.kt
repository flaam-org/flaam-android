package com.minor_project.flaamandroid.ui.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _resetPassResult = MutableLiveData<ApiResponse<String>>()
    val resetPassResult: LiveData<ApiResponse<String>> = _resetPassResult

    fun getResetPasswordToken(email: String) {
        viewModelScope.launch {
            val res = authRepository.getResetPasswordToken(
                RegisterLoginRequest(
                    null,
                    null,
                    null,
                    null,
                    email,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )

            _resetPassResult.postValue(handleGetResponse(res))
        }
    }


}