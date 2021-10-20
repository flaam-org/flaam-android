package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.*

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)

    suspend fun getTagsList() = api.getTagsList(null, null, null)

    suspend fun getTagsForKeyword(keyword: String?, ids: String?) =
        api.getTagsList(keyword, ids, null)

    suspend fun getUserFavouriteTags(favouritedBy: Int?) = api.getTagsList(null, null, favouritedBy)

    suspend fun createNewTag(body: TagsRequest) = api.createNewTag(body)

    suspend fun postIdea(body: PostIdeaRequest) = api.postIdea(body)

    suspend fun updateIdea(body: PostIdeaRequest, id: Int) = api.updateIdea(body, id)

    suspend fun deleteIdea(id: Int, body: DeleteIdeaRequest) = api.deleteIdea(id, body)

    suspend fun getIdeas(
        limit: Int?,
        offset: Int?,
        ownerId: Int?,
        bookmarkedBy: Int?,
        ordering: String? = null,
        tags: String? = null,
        search: String? = null
    ) = api.getIdeas(limit, offset, ownerId, bookmarkedBy, ordering, tags, search)

    suspend fun getIdeaDetails(id: Int) = api.getIdeaDetails(id)

    suspend fun addIdeaToUsersBookmarks(id: String) = api.addIdeaToUsersBookmarks(id)

    suspend fun removeIdeaFromUsersBookmarks(id: String) = api.removeIdeaFromUsersBookmarks(id)

    suspend fun upvoteIdea(id: String) = api.upvoteIdea(id, 1)

    suspend fun downvoteIdea(id: String) = api.downvoteIdea(id, -1)

    suspend fun addTagToUsersFavouriteTags(id: String) = api.addTagToUsersFavouriteTags(id)

    suspend fun removeTagFromUsersFavouriteTags(id: String) =
        api.removeTagFromUsersFavouriteTags(id)

    suspend fun addImplementation(body: AddImplementationRequest) = api.addImplementation(body)


}