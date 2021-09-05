package com.minor_project.flaamandroid.feed.userprofile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class UserProfileViewPagerAdapter(fm: FragmentManager?, var mNumOfTabs: Int) :
    FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
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

    override fun getCount(): Int {
        return mNumOfTabs
    }
}