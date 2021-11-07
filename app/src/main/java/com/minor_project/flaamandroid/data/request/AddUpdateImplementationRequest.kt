package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddUpdateImplementationRequest(
    @Json(name = "body")
    val body: String?,
    @Json(name = "completed_milestones")
    val completedMilestones: List<String>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "draft")
    val draft: Boolean?,
    @Json(name = "idea")
    val idea: Int?,
    @Json(name = "is_accepted")
    val isAccepted: Boolean?,
    @Json(name = "is_validated")
    val isValidated: Boolean?,
    @Json(name = "repo_url")
    val repoUrl: String?,
    @Json(name = "title")
    val title: String?
)