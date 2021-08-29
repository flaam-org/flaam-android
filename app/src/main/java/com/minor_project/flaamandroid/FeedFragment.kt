package com.minor_project.flaamandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedPostList = ArrayList<FeedPostModel>()

        val n = FeedPostModel(R.drawable.ic_add_24dp,"soumya jain", 2, 4, "fjgntyhj frvktjtyb", "ejfnerjtn")

        feedPostList.add(n)
        binding.rvFeedPosts.layoutManager = LinearLayoutManager(context)

        binding.rvFeedPosts.setHasFixedSize(true)


        val feedPostAdapter = FeedPostAdapter(requireContext(), feedPostList)
        binding.rvFeedPosts.adapter = feedPostAdapter
    }
}