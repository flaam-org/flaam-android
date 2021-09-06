package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsRequest(
    @Json(name = "description")
    val description: String?,
    @Json(name = "name")
    val name: String
)