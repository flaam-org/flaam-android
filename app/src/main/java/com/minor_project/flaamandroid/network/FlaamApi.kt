package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface FlaamApi {

    @GET("accounts/user/profile")
    suspend fun getUserProfile(): Response<ViewProfileResponse>

    @PUT("accounts/user/profile")
    suspend fun updateUserProfile(@Body body: UpdateProfileRequest): Response<UpdateProfileResponse>

    @GET("tags")
    suspend fun getTagsList(
        @Query("name") keyword: String?,
        @Query("ids") ids: String?
    ): Response<TagsResponse>


    @POST("tags")
    suspend fun createNewTag(@Body body: TagsRequest): Response<TagsResponseItem>

    @POST("ideas")
    suspend fun postIdea(@Body body: PostIdeaRequest): Response<IdeasResponse.Result>

    @GET("ideas")
    suspend fun getIdeas(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<IdeasResponse>

    @POST("idea/{id}/bookmark")
    suspend fun addIdeaToUsersBookmarks(@Path("id") id: String) : Response<Unit>

    @DELETE("idea/{id}/bookmark")
    suspend fun removeIdeaFromUsersBookmarks(@Path("id") id: String) : Response<Unit>


}