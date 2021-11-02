package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostCommentRequest(
    @Json(name = "body")
    val body: String?,
    @Json(name = "discussion")
    val discussion: Int?
)