package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.UpdateProfileRequest

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)

    suspend fun getTagsList() = api.getTagsList(null, null)

    suspend fun getTagsForKeyword(keyword: String?, ids: String?) = api.getTagsList(keyword,ids)



}