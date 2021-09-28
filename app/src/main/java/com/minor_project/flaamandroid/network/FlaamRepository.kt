package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import retrofit2.http.Query

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)

    suspend fun getTagsList() = api.getTagsList(null, null)

    suspend fun getTagsForKeyword(keyword: String?, ids: String?) = api.getTagsList(keyword, ids)

    suspend fun createNewTag(body: TagsRequest) = api.createNewTag(body)

    suspend fun postIdea(body: PostIdeaRequest) = api.postIdea(body)

    suspend fun getIdeas(limit: Int, offset: Int) = api.getIdeas(5, offset)


}