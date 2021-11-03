package com.minor_project.flaamandroid.network

import com.minor_project.flaamandroid.data.request.*
import com.minor_project.flaamandroid.data.response.CommentsForDiscussionResponse
import retrofit2.Response
import retrofit2.http.Query

class FlaamRepository(private val api: FlaamApi) {

    suspend fun getUserProfile() = api.getUserProfile()

    suspend fun updateUserProfile(body: UpdateProfileRequest) = api.updateUserProfile(body)

    suspend fun getTagsList() = api.getTagsList(null, null, null)

    suspend fun getTagsForKeyword(keyword: String?, ids: String?) =
        api.getTagsList(keyword, ids, null)

    suspend fun getUserFavouriteTags(favouritedBy: Int?) = api.getTagsList(null, null, favouritedBy)

    suspend fun createNewTag(body: TagsRequest) = api.createNewTag(body)

    suspend fun postIdea(body: CreateUpdateIdeaRequest) = api.postIdea(body)

    suspend fun updateIdea(body: CreateUpdateIdeaRequest, id: Int) = api.updateIdea(body, id)

    suspend fun deleteIdea(id: Int) = api.deleteIdea(id)

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

    suspend fun voteIdea(id: String, value: Int) = api.upvoteIdea(id, value)

    suspend fun addTagToUsersFavouriteTags(id: String) = api.addTagToUsersFavouriteTags(id)

    suspend fun removeTagFromUsersFavouriteTags(id: String) =
        api.removeTagFromUsersFavouriteTags(id)

    suspend fun addImplementation(body: AddImplementationRequest) = api.addImplementation(body)

    suspend fun voteImplementation(id: String, value: Int) = api.voteImplementation(id, value)

    suspend fun getImplementations(ideaId: String?, ownerId: String?) =
        api.getImplementations(ideaId, ownerId)

    suspend fun deleteImplementation(implementationId: Int) =
        api.deleteImplementation(implementationId)

    suspend fun createDiscussion(body: CreateDiscussionRequest) = api.createDiscussion(body)

    suspend fun getDiscussions(ideaId: String) = api.getDiscussions(ideaId)

    suspend fun getDiscussionDetails(id: Int) = api.getDiscussionDetails(id)

    suspend fun voteDiscussion(discussionId: String, value: Int) =
        api.upvoteDiscussion(discussionId, value)

    suspend fun downvoteDiscussion(discussionId: String) = api.downvoteDiscussion(discussionId, -1)

    suspend fun getCommentsForDiscussion(
        id: Int
    ) = api.getCommentsForDiscussion(id)

    suspend fun postCommentForDiscussion(
        body: PostCommentRequest
    ) = api.postCommentForDiscussion(body)


}