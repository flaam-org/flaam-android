package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)

    suspend fun getTagsList() = api.getTagsList(null, null, null)

    suspend fun getTagsForKeyword(keyword: String?, ids: String?) =
        api.getTagsList(keyword, ids, null)

    suspend fun getUserFavouriteTags(favouritedBy: Int?) = api.getTagsList(null, null, favouritedBy)

    suspend fun createNewTag(body: TagsRequest) = api.createNewTag(body)

    suspend fun postIdea(body: PostIdeaRequest) = api.postIdea(body)

    suspend fun getIdeas(limit: Int?, offset: Int?, ownerId: Int?, bookmarkedBy: Int?) =
        api.getIdeas(limit, offset, ownerId, bookmarkedBy)

    suspend fun getIdeaDetails(id: Int) = api.getIdeaDetails(id)

    suspend fun addIdeaToUsersBookmarks(id: String) = api.addIdeaToUsersBookmarks(id)

    suspend fun removeIdeaFromUsersBookmarks(id: String) = api.removeIdeaFromUsersBookmarks(id)

    suspend fun addTagToUsersFavouriteTags(id: String) = api.addTagToUsersFavouriteTags(id)

    suspend fun removeTagFromUsersFavouriteTags(id: String) =
        api.removeTagFromUsersFavouriteTags(id)


}