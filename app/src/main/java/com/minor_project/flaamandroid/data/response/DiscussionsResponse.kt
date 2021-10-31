package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DiscussionsResponse(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: Any?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val results: List<Result>?
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "body")
        val body: String?,
        @Json(name = "comments_count")
        val commentsCount: Int?,
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "draft")
        val draft: Boolean?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "idea")
        val idea: Int?,
        @Json(name = "owner")
        val owner: Int?,
        @Json(name = "owner_avatar")
        val ownerAvatar: String?,
        @Json(name = "owner_username")
        val ownerUsername: String?,
        @Json(name = "title")
        val title: String?,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "viewed")
        val viewed: Boolean?,
        @Json(name = "vote")
        val vote: Int?
    )
}