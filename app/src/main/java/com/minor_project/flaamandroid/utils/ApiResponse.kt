package com.minor_project.flaamandroid.utils

sealed class ApiResponse<T> {

    data class Success <T>(val body: T) : ApiResponse<T>()
    class Loading <T>() : ApiResponse<T>()
    data class Error<T>(val message: String?,val body: T? = null,val status: Int): ApiResponse<T>()
}