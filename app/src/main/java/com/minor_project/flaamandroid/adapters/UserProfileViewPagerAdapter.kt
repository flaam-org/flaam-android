package com.minor_project.flaamandroid.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.minor_project.flaamandroid.ui.userprofile.tabs.MyBookmarksFragment
import com.minor_project.flaamandroid.ui.userprofile.tabs.MyIdeasFragment
import com.minor_project.flaamandroid.ui.userprofile.tabs.MyImplementationsFragment


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
