package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import kotlinx.coroutines.coroutineScope

class AuthRepository(private val api: AuthApi) {

    suspend fun refreshToken(body: LoginResponse) = api.refreshToken(body)

    suspend fun postLogin(body: LoginRequest) = api.postLogin(body)

    suspend fun registerUser(body: LoginRequest) = api.registerUser(body)


}