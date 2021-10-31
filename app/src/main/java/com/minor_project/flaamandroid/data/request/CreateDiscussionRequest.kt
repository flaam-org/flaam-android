package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateDiscussionRequest(
    @Json(name = "body")
    val body: String,
    @Json(name = "draft")
    val draft: Boolean,
    @Json(name = "idea")
    val idea: Int,
    @Json(name = "title")
    val title: String
)