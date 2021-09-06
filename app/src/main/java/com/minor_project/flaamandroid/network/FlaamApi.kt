package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface FlaamApi {

    @GET("user/profile")
    suspend fun getUserProfile(): Response<ViewProfileResponse>

    @PUT("user/profile")
    suspend fun updateUserProfile(@Body body: UpdateProfileRequest) : Response<UpdateProfileResponse>


    @GET("/tags")
    suspend fun getTagsList() : Response<TagsResponse>

}