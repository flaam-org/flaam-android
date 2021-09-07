package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.data.response.UpdateProfileResponse
import com.minor_project.flaamandroid.data.response.ViewProfileResponse
import retrofit2.Response
import retrofit2.http.*

interface FlaamApi {

    @GET("account/user/profile")
    suspend fun getUserProfile(): Response<ViewProfileResponse>

    @PUT("account/user/profile")
    suspend fun updateUserProfile(@Body body: UpdateProfileRequest) : Response<UpdateProfileResponse>

    @GET("tags")
    suspend fun getTagsList(@Query("name") keyword: String?) : Response<TagsResponse>

//    @GET("tags/{name}")
//    suspend fun getTagsForKeyword(@Path("name") keyword: String): Response<TagsResponse>

}