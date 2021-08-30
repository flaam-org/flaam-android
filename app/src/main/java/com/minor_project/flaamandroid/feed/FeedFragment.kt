package com.minor_project.flaamandroid.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.adapters.FeedPostAdapter
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding
import com.minor_project.flaamandroid.models.FeedPostModel

class FeedFragment : Fragment() {

    private lateinit var binding : FragmentFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedBinding.inflate(inflater)

        binding.apply {

            binding.efabPostIdea.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostIdeaFragment())
            }

            binding.civFeedFragmentMyProfile.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToMyProfileFragment())
            }

        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedPostList = ArrayList<FeedPostModel>()

        val n = FeedPostModel(
            R.drawable.ic_profile_image_place_holder,"soumya jain", 2, 4, resources.getString(
                R.string.sample_text
            ))

        feedPostList.add(n)
        binding.rvFeedPosts.layoutManager = LinearLayoutManager(context)

        binding.rvFeedPosts.setHasFixedSize(true)


        val feedPostAdapter = FeedPostAdapter(requireContext(), feedPostList)
        binding.rvFeedPosts.adapter = feedPostAdapter
    }
}