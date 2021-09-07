package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import retrofit2.Response
import retrofit2.http.Path

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)


    suspend fun getTagsList() = api.getTagsList(null)

    suspend fun getTagsForKeyword(keyword: String) = api.getTagsList(keyword)



}