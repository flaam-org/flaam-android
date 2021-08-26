package com.minor_project.flaamandroid.network

import android.content.Context
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiBuilder(private val context: Context) {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun getFlaamApi(userPreferences: UserPreferences): FlaamApi {

        val client = OkHttpClient().newBuilder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()

                request.newBuilder().apply {
                    addHeader("Content-Type", "application/json")

                    val token = runBlocking { userPreferences.getToken().first() }

                    if(token != null){
                        addHeader("Authorization", "Bearer $token")
                    }
                }
                return@addInterceptor it.proceed(request)
            }.addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(FlaamApi::class.java)
    }



}