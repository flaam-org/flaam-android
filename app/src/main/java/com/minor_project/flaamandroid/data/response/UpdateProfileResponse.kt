package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileResponse(
    @Json(name = "avatar")
    val avatar: String?,
    @Json(name = "bookmarked_ideas")
    val bookmarkedIdeas: List<Any>?,
    @Json(name = "bookmarked_implementations")
    val bookmarkedImplementations: List<Any>?,
    @Json(name = "date_joined")
    val dateJoined: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "favourite_tags")
    val favouriteTags: List<Any>?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "followers")
    val followers: List<Any>?,
    @Json(name = "following")
    val following: List<Any>?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "last_login")
    val lastLogin: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "show_email")
    val showEmail: Boolean?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "username")
    val username: String?
)