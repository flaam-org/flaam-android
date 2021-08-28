package com.minor_project.flaamandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding

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

}