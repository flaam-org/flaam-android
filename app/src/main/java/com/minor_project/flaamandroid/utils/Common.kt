package com.minor_project.flaamandroid.utils

import retrofit2.Response
import timber.log.Timber


fun <T> handleGetResponse(response: Response<T>): ApiException<T> {
    if (response.code() == 200) {
        response.body().let {
            Timber.e(response.body().toString())
            return ApiException.Success(it!!)
        }
    }

    Timber.e(response.errorBody()?.string().toString())
    return ApiException.Error(message = response.message(), status = response.code())
}


fun <T> handlePostResponse(response: Response<T>): ApiException<T> {
    if (response.code() == 201) {
        response.body().let {
            Timber.e(response.body().toString())
            return ApiException.Success(it!!)
        }
    }

    Timber.e(response.errorBody()?.string().toString())
    return ApiException.Error(message = response.message(), status = response.code())
}

