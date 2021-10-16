package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

class AuthRepository(private val api: AuthApi) {

    suspend fun refreshToken(body: RegisterLoginResponse) = api.refreshToken(body)

    suspend fun postLogin(body: RegisterLoginRequest) = api.postLogin(body)

    suspend fun registerUser(body: RegisterLoginRequest) = api.registerUser(body)


    suspend fun getResetPasswordToken(body: RegisterLoginRequest) = api.getResetPasswordToken(body)





}