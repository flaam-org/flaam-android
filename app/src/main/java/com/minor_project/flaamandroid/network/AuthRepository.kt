package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse

class AuthRepository(private val api: AuthApi) {

    suspend fun refreshToken(body: RegisterLoginResponse) = api.refreshToken(body)

    suspend fun postLogin(body: RegisterLoginRequest) = api.postLogin(body)

    suspend fun registerUser(body: RegisterLoginRequest) = api.registerUser(body)


}