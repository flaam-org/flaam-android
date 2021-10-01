package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ViewProfileResponse(
    @Json(name = "avatar")
    val avatar: String?,
    @Json(name = "bookmarked_ideas")
    val bookmarkedIdeas: List<Int>?,
    @Json(name = "date_joined")
    val dateJoined: String?,
    @Json(name = "description")
    val description: Any?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "favourite_tags")
    val favouriteTags: List<Int>?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "following")
    val following: List<Any>?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "last_login")
    val lastLogin: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "status")
    val status: Any?,
    @Json(name = "username")
    val username: String?
)