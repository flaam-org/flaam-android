package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface FlaamApi {

    @GET("user/profile")
    suspend fun getUserProfile(): Response<ViewProfileResponse>

}