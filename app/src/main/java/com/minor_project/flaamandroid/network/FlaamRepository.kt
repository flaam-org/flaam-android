package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.LoginRequest

class FlaamRepository(private val api: FlaamApi) {

    suspend fun postLogin(body: LoginRequest) = api.postLogin(body)


    suspend fun registerUser(body: LoginRequest) = api.registerUser(body)


}