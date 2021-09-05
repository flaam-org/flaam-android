package com.minor_project.flaamandroid.utils

import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


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



fun String.getDaysDiff(): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    val date = sdf.parse(this.substring(0, this.length - 13) + this.substring(this.length - 6))
    val today = Date()

    return (((date!!.time - today.time) / (1000 * 60 * 60 * 24) % 7)).toInt()



}