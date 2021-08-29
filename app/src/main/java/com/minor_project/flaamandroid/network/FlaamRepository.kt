package com.minor_project.flaamandroid.network

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

}