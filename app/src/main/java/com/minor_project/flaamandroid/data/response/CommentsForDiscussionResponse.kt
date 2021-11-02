package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsForDiscussionResponse(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: Any?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val results: List<Comments?>?
) {
    @JsonClass(generateAdapter = true)
    data class Comments(
        @Json(name = "body")
        val body: String?,
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "discussion")
        val discussion: Int?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "owner")
        val owner: Int?,
        @Json(name = "owner_avatar")
        val ownerAvatar: String?,
        @Json(name = "owner_username")
        val ownerUsername: String?,
        @Json(name = "updated_at")
        val updatedAt: String?
    )
}