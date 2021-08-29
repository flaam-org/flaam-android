package com.minor_project.flaamandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.FeedFragmentDirections
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding
import com.minor_project.flaamandroid.databinding.FragmentPostIdeaBinding

class PostIdeaFragment : Fragment() {

    private lateinit var binding : FragmentPostIdeaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostIdeaBinding.inflate(inflater)

        binding.apply {

            binding.ivPostIdeaClose.setOnClickListener {
                findNavController().navigate(PostIdeaFragmentDirections.actionPostIdeaFragmentToFeedFragment())
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}