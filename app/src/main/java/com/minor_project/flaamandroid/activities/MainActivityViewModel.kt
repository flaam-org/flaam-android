package com.minor_project.flaamandroid.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse
import com.minor_project.flaamandroid.network.AuthRepository
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.handleGetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val prefs: UserPreferences
) : ViewModel() {

    val refreshTokenResult = MutableLiveData<ApiResponse<RegisterLoginResponse>>()

    fun refreshToken() {

        viewModelScope.launch {
            val res = repo.refreshToken(RegisterLoginResponse(null, prefs.refreshToken.first()))

            refreshTokenResult.postValue(handleGetResponse(res))
        }

    }

}