package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "description")
    val description: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "favourite_tags")
    val favouriteTags: List<Int>?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "password")
    val password: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "username")
    val username: String?
)