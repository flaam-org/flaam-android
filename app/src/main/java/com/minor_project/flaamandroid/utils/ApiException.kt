package com.minor_project.flaamandroid.utils

import retrofit2.Response
import timber.log.Timber

sealed class ApiException<T> {

    data class Success <T>(val body: T) : ApiException<T>()
    class Loading <T>() : ApiException<T>()
    data class Error<T>(val message: String?,val body: T? = null,val status: Int): ApiException<T>()
}