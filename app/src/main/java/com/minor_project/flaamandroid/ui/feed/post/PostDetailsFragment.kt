package com.minor_project.flaamandroid.ui.feed.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.adapters.PostDetailsViewPagerAdapter
import com.minor_project.flaamandroid.databinding.FragmentPostDetailsBinding


class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostDetailsBinding.inflate(inflater)

        val tabLayout = binding.tabLayoutPostDetails


        val adapter = PostDetailsViewPagerAdapter(this)
        binding.viewPagerPostDetails.adapter = adapter


        TabLayoutMediator(
            tabLayout, binding.viewPagerPostDetails
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Projects"
                2 -> tab.text = "Discussion"
            }
        }.attach()

        return binding.root
    }
}