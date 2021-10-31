package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateDiscussionResponse(
    @Json(name = "body")
    val body: String?,
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