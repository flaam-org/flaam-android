package com.minor_project.flaamandroid.ui.feed.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.adapters.PostDetailsViewPagerAdapter
import com.minor_project.flaamandroid.databinding.FragmentPostDetailsBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding

    private val viewModel: PostDetailsViewModel by viewModels()

    private val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val ideaId = args.ideaId

        binding = FragmentPostDetailsBinding.inflate(inflater)

        binding.ivBackPostDetails.setOnClickListener {
            findNavController().popBackStack()
        }

        val tabLayout = binding.tabLayoutPostDetails


        val adapter = PostDetailsViewPagerAdapter(this, ideaId)
        binding.viewPagerPostDetails.adapter = adapter


        TabLayoutMediator(
            tabLayout, binding.viewPagerPostDetails
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Implementations"
                2 -> tab.text = "Discussion"
            }
        }.attach()

        initObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ideaId = args.ideaId
        makeToast(ideaId.toString())
    }

    private fun initObservers() {
        viewModel.getIdeaDetails(args.ideaId)
        viewModel.ideaDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
//                    binding.apply {
//                        tvPostDetailsTitle.text = it.body.title.toString()
//                    }
                }
            }
        }
    }
}