package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.data.response.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("accounts/login")
    suspend fun postLogin(
        @Body body: LoginRequest,
    ): Response<LoginResponse>

    @POST("accounts/user")
    suspend fun registerUser(
        @Body body: LoginRequest
    ): Response<RegisterUserResponse>




    // auth requests

    @POST("accounts/login/refresh")
    suspend fun refreshToken(
        @Body body: LoginResponse
    ): Response<LoginResponse>




}