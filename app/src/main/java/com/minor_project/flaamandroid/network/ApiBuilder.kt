package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiBuilder {

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun getFlaamApi(): FlaamApi {

        val client = OkHttpClient().newBuilder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()
                request.newBuilder().apply {
                    addHeader("Content-Type", "application/json")
                }
                return@addInterceptor it.proceed(request)
            }.addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory::class).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(FlaamApi::class.java)
    }



}