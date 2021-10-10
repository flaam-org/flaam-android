package com.minor_project.flaamandroid.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterLoginResponse(
    @Json(name = "access")
    val access: String?,
    @Json(name = "refresh")
    val refresh: String?
)