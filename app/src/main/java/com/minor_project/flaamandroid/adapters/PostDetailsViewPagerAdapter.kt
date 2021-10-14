package com.minor_project.flaamandroid.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostDescriptionFragment
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostDiscussionFragment
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostProjectsFragment

class PostDetailsViewPagerAdapter(frag: Fragment, ideaId : Int) :
    FragmentStateAdapter(frag) {

    private val mIdeaId = ideaId

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PostDescriptionFragment(mIdeaId)
            }
            1 -> {
                PostProjectsFragment(mIdeaId)
            }
            2 -> {
                PostDiscussionFragment(mIdeaId)
            }
            else -> PostDescriptionFragment(mIdeaId)
        }
    }

}