package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdeasResponse(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: Any?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val ideaResponseItems: List<IdeaResponseItem>?
)