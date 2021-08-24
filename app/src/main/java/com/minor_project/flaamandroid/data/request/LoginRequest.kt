package com.minor_project.flaamandroid.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "password")
    val password: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "email")
    val email: String?

)