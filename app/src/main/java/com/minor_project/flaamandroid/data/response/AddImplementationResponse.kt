package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddImplementationResponse(
    @Json(name = "body")
    val body: String?,
    @Json(name = "bookmarked")
    val bookmarked: Boolean?,
    @Json(name = "completed_milestones")
    val completedMilestones: List<String>?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "draft")
    val draft: Boolean?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "idea")
    val idea: Int?,
    @Json(name = "is_accepted")
    val isAccepted: Boolean?,
    @Json(name = "is_validated")
    val isValidated: Boolean?,
    @Json(name = "milestones")
    val milestones: List<Any>?,
    @Json(name = "owner")
    val owner: Int?,
    @Json(name = "owner_avatar")
    val ownerAvatar: String?,
    @Json(name = "owner_username")
    val ownerUsername: String?,
    @Json(name = "tags")
    val tags: List<Any>?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "viewed")
    val viewed: Boolean?,
    @Json(name = "vote")
    val vote: Int?
)