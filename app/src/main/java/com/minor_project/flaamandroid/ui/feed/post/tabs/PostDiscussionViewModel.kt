package com.minor_project.flaamandroid.ui.feed.post.tabs

import androidx.lifecycle.ViewModel
import com.minor_project.flaamandroid.network.FlaamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDiscussionViewModel @Inject constructor(private val flaamRepo: FlaamRepository) :
    ViewModel() {
}