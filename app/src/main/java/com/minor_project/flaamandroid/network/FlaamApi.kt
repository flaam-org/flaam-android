package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.data.response.RegisterUserResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FlaamApi {

    @GET("user/profile")
    suspend fun getUserProfile(): Response<ViewProfileResponse>

}