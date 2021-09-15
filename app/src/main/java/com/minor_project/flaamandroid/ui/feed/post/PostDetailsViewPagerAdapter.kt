package com.minor_project.flaamandroid.ui.feed.post

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PostDetailsViewPagerAdapter(frag: Fragment) :
    FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PostDescriptionFragment()
            }
            1 -> {
                PostProjectsFragment()
            }
            2 -> {
                PostDiscussionFragment()
            }
            else -> PostDescriptionFragment()
        }
    }

}