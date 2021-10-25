package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteIdeaRequest(
    @Json(name = "archived")
    val archived: Boolean,
    @Json(name = "body")
    val body: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "draft")
    val draft: Boolean,
    @Json(name = "milestones")
    val milestones: List<List<String>>,
    @Json(name = "tags")
    val tags: List<Int>,
    @Json(name = "title")
    val title: String
)