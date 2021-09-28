package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdeaResponseItem(
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
    @Json(name = "milestones")
    val milestones: List<Any>?,
    @Json(name = "owner")
    val owner: Int?,
    @Json(name = "tags")
    val tags: List<Int>?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "upvotes_count")
    val upvotesCount: Int?,
    @Json(name = "vote")
    val vote: String?
)