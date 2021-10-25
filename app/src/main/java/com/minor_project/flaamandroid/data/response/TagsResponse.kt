package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsResponse(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: String?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val results: List<Result>?
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "description")
        val description: String?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "updated_at")
        val updatedAt: String?
    )
}