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

    @PATCH("accounts/user/profile")
    suspend fun updateUserProfile(@Body body: UpdateProfileRequest): Response<UpdateProfileResponse>

    @GET("tags")
    suspend fun getTagsList(
        @Query("name") keyword: String?,
        @Query("ids") ids: String?,
        @Query("favourited_by") favouritedBy : Int?
    ): Response<TagsResponse>


    @POST("tags")
    suspend fun createNewTag(@Body body: TagsRequest): Response<TagsResponse.Result>

    @POST("ideas")
    suspend fun postIdea(@Body body: PostIdeaRequest): Response<IdeasResponse.Result>

    @GET("ideas")
    suspend fun getIdeas(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("owner") ownerId: Int?,
        @Query("bookmarked_by") bookmarkedBy: Int?,
        @Query("ordering") ordering: String? = null,
        @Query("tags") tags: String? = null,
        @Query("search") search: String? = null
    ): Response<IdeasResponse>


    @GET("idea/{id}")
    suspend fun getIdeaDetails(@Path("id") id : Int) : Response<IdeasResponse.Result>

    @POST("idea/{id}/bookmark")
    suspend fun addIdeaToUsersBookmarks(@Path("id") id: String): Response<Unit>

    @DELETE("idea/{id}/bookmark")
    suspend fun removeIdeaFromUsersBookmarks(@Path("id") id: String): Response<Unit>

    @POST("idea/{id}/vote")
    suspend fun upvoteIdea(@Path("id") id : String, @Query("value") value : Int) : Response<Unit>

    @POST("idea/{id}/vote")
    suspend fun downvoteIdea(@Path("id") id : String, @Query("value") value : Int) : Response<Unit>

    @POST("tag/{id}/favourite")
    suspend fun addTagToUsersFavouriteTags(@Path("id") id: String): Response<Unit>

    @DELETE("tag/{id}/favourite")
    suspend fun removeTagFromUsersFavouriteTags(@Path("id") id: String): Response<Unit>


}