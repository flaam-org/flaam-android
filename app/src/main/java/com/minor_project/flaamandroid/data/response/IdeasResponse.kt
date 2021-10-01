package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdeasResponse(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: Any?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val results: List<Result?>?
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "body")
        val body: String?,
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "description")
        val description: String?,
        @Json(name = "downvotes_count")
        val downvotesCount: Int?,
        @Json(name = "draft")
        val draft: Boolean?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "implementations_count")
        val implementationsCount: Int?,
        @Json(name = "milestones")
        val milestones: List<Any?>?,
        @Json(name = "owner")
        val owner: Int?,
        @Json(name = "owner_avatar")
        val ownerAvatar: String?,
        @Json(name = "owner_username")
        val ownerUsername: String?,
        @Json(name = "tags")
        val tags: List<Tag?>?,
        @Json(name = "title")
        val title: String?,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "upvotes_count")
        val upvotesCount: Int?,
        @Json(name = "view_count")
        val viewCount: Int?,
        @Json(name = "viewed")
        val viewed: Boolean?,
        @Json(name = "bookmarked")
        val bookmarked: Boolean,
        @Json(name = "vote")
        val vote: Any?
    ) {
        @JsonClass(generateAdapter = true)
        data class Tag(
            @Json(name = "id")
            val id: Int?,
            @Json(name = "name")
            val name: String?
        )
    }
}