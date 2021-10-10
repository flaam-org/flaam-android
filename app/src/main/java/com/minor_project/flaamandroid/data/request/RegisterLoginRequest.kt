package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterLoginRequest(
    @Json(name = "avatar")
    val avatar: String?,
    @Json(name = "bookmarked_ideas")
    val bookmarkedIdeas: List<Int>?,
    @Json(name = "bookmarked_implementations")
    val bookmarkedImplementations: List<Int>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "favourite_tags")
    val favouriteTags: List<Int>?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "followers")
    val followers: List<Int>?,
    @Json(name = "following")
    val following: List<Int>?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "password")
    val password: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "username")
    val username: String?
)