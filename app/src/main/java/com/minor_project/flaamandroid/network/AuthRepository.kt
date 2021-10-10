package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse

class AuthRepository(private val api: AuthApi) {

    suspend fun refreshToken(body: LoginResponse) = api.refreshToken(body)

    suspend fun postLogin(body: RegisterLoginRequest) = api.postLogin(body)

    suspend fun registerUser(body: RegisterLoginRequest) = api.registerUser(body)


}