package com.minor_project.flaamandroid.feed.userprofile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class UserProfileViewPagerAdapter(frag: Fragment) :
    FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
            return when (position) {
        0 -> {
            MyBookmarksFragment()
        }
        1 -> {
            MyIdeasFragment()
        }
        2 -> {
            MyImplementationsFragment()
        }
        else -> MyBookmarksFragment()
    }
    }

}
