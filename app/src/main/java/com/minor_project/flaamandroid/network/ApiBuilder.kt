package com.minor_project.flaamandroid.network

import android.content.Context
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ApiBuilder(private val context: Context) {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun getFlaamApi(userPreferences: UserPreferences, authRepo: AuthRepository): FlaamApi {

        Timber.e("getflaamapi")

        val client = OkHttpClient().newBuilder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor {

                Timber.e("intercepted")

                val originalRequest: Request = it.request()

                var newRequest = originalRequest.newBuilder().apply {
                    addHeader("Content-Type", "application/json")

                    val token = runBlocking { userPreferences.accessToken.first() }

                    if(token != null){
                        addHeader("Authorization", "Bearer $token")
                    }
                }.build()

                var res = it.proceed(newRequest)
                Timber.e(res.code.toString())
                if(res.code == 401){
                    runBlocking {
                        Timber.e("runblocking init")

                        val response = authRepo.refreshToken(LoginResponse(null, userPreferences.refreshToken.first()))

                        Timber.e(response.toString() + "body here")
                        if(response.code() == 200){
                            userPreferences.updateTokens(response.body()!!)
                            newRequest = newRequest.newBuilder().addHeader("Authorization", "Bearer ${userPreferences.accessToken.first()}").build()
                            res = it.proceed(newRequest)
                            Timber.e(res.code.toString())
                        }else{
                        }
                    }
                }

                Timber.e(originalRequest.headers.toString() + "headers here")

                return@addInterceptor res
            }.addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(FlaamApi::class.java)
    }


    fun getFlaamApiForAuthRepo(): AuthApi {

        Timber.e("authapi")

        val client = OkHttpClient().newBuilder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor {
                val request = it.request()

                request.newBuilder().apply {
                    addHeader("Content-Type", "application/json")
                }.build()

                return@addInterceptor it.proceed(request)
            }.addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(AuthApi::class.java)
    }



}