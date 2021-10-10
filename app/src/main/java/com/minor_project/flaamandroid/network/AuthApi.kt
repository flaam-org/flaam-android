package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.RegisterLoginRequest
import com.minor_project.flaamandroid.data.response.RegisterLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("accounts/login")
    suspend fun postLogin(
        @Body body: RegisterLoginRequest,
    ): Response<RegisterLoginResponse>

    @POST("accounts/users")
    suspend fun registerUser(
        @Body body: RegisterLoginRequest
    ): Response<RegisterLoginResponse>




    // auth requests

    @POST("accounts/login/refresh")
    suspend fun refreshToken(
        @Body body: RegisterLoginResponse
    ): Response<RegisterLoginResponse>




}