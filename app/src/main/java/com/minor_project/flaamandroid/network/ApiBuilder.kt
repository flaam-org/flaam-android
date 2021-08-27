package com.minor_project.flaamandroid.network

import android.content.Context
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ApiBuilder(private val context: Context) {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun getFlaamApi(userPreferences: UserPreferences, authRepo: AuthRepository): FlaamApi {

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

                val res = it.proceed(request)

                if(res.code == 401){
                    runBlocking {
                        val response = authRepo.refreshToken(LoginResponse(null, userPreferences.getToken().last()))
                        if(response.code() == 201){
                            request.newBuilder().addHeader("Authorization", "Bearer ${userPreferences.getToken().first()}")
                        }else{
                            // TODO: logout user
                        }
                    }
                }

                return@addInterceptor res
            }.addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(FlaamApi::class.java)
    }


    fun getFlaamApiForAuthRepo(): AuthApi {

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
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).baseUrl(Constants.BASE_URL).build()

        return retrofit.create(AuthApi::class.java)
    }



}