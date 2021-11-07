package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.*
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
        @Query("favourited_by") favouritedBy: Int?
    ): Response<TagsResponse>


    @POST("tags")
    suspend fun createNewTag(@Body body: TagsRequest): Response<TagsResponse.Result>

    @POST("ideas")
    suspend fun postIdea(@Body body: CreateUpdateIdeaRequest): Response<IdeasResponse.Result>

    @PATCH("idea/{id}")
    suspend fun updateIdea(
        @Body body: CreateUpdateIdeaRequest,
        @Path("id") id: Int
    ): Response<IdeasResponse.Result>


    @DELETE("idea/{id}")
    suspend fun deleteIdea(
        @Path("id") id: Int
    ): Response<Any>

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
    suspend fun getIdeaDetails(@Path("id") id: Int): Response<IdeasResponse.Result>

    @POST("idea/{id}/bookmark")
    suspend fun addIdeaToUsersBookmarks(@Path("id") id: String): Response<Unit>

    @DELETE("idea/{id}/bookmark")
    suspend fun removeIdeaFromUsersBookmarks(@Path("id") id: String): Response<Unit>

    @POST("idea/{id}/vote")
    suspend fun upvoteIdea(@Path("id") id: String, @Query("value") value: Int): Response<Unit>


    @POST("tag/{id}/favourite")
    suspend fun addTagToUsersFavouriteTags(@Path("id") id: String): Response<Unit>

    @DELETE("tag/{id}/favourite")
    suspend fun removeTagFromUsersFavouriteTags(@Path("id") id: String): Response<Unit>

    @POST("implementations")
    suspend fun addImplementation(@Body body: AddUpdateImplementationRequest): Response<AddImplementationResponse>

    @POST("implementation/{id}/vote")
    suspend fun voteImplementation(
        @Path("id") implementationId: String,
        @Query("value") value: Int
    ): Response<Unit>

    @GET("implementations")
    suspend fun getImplementations(
        @Query("idea") ideaId: String?,
        @Query("owner") ownerId: String?
    ): Response<ImplementationsResponse>

    @GET("implementation/{id}")
    suspend fun getImplementationDetails(
        @Path("id") implementationId: Int
    ): Response<ImplementationsResponse.Result>

    @PATCH("implementation/{id}")
    suspend fun updateImplementation(
        @Path("id") implementationId: Int,
        @Body body: AddUpdateImplementationRequest
    ): Response<ImplementationsResponse.Result>

    @DELETE("implementation/{id}")
    suspend fun deleteImplementation(
        @Path("id") implementationId: Int
    ): Response<Unit>


    @POST("discussions")
    suspend fun createDiscussion(@Body body: CreateDiscussionRequest): Response<CreateDiscussionResponse>

    @GET("discussions")
    suspend fun getDiscussions(@Query("idea") ideaId: String): Response<DiscussionsResponse>

    @GET("discussion/{id}")
    suspend fun getDiscussionDetails(@Path("id") id: Int): Response<DiscussionsResponse.Result>

    @POST("discussion/{id}/vote")
    suspend fun upvoteDiscussion(
        @Path("id") discussionId: String,
        @Query("value") value: Int

    ): Response<Unit>

    @POST("discussion/{id}/vote")
    suspend fun downvoteDiscussion(
        @Path("id") discussionId: String,
        @Query("value") value: Int

    ): Response<Unit>

    @GET("discussion/comments")
    suspend fun getCommentsForDiscussion(
        @Query("discussion") id: Int
    ): Response<CommentsForDiscussionResponse>

    @POST("discussion/comments")
    suspend fun postCommentForDiscussion(
        @Body body: PostCommentRequest
    ): Response<Any>


}