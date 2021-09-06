package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsResponseItem(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)