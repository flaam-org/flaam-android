package com.minor_project.flaamandroid.ui.userprofile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.FragmentViewProfileBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ViewProfileFragment : Fragment() {

    private val viewModel: ViewProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences


    private lateinit var binding: FragmentViewProfileBinding

    private val args: ViewProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProfileBinding.inflate(inflater)
        initObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
    }

    private fun initObservers() {
        val shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()
        viewModel.getUserProfileFromUsername(args.username)
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE

                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {
                    binding.apply {
                        lifecycleScope.launch(Dispatchers.IO) {
                            civFragmentViewProfileUserImage.loadSVG(it.body.avatar.toString())
                        }

                        tvUsernameFragmentViewProfile.text = it.body.username.toString()
                        tvFnameFragmentViewProfile.text = it.body.firstName.toString()
                        tvLnameFragmentViewProfile.text = it.body.lastName.toString()
                        viewModel.getUserFavouriteTags(it.body.id!!)
                    }
                }
            }
        }


        viewModel.userFavouriteTagsList.observe(viewLifecycleOwner)
        { res ->
            when (res) {
                is ApiResponse.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    makeToast("Unable to fetch Favourite Tags!")
                }
                is ApiResponse.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    if (res.body.results.isNullOrEmpty()) {
                        binding.tvFragmentViewProfileNoUserFavouriteTagsToDisplay.visible()

                    } else {
                        binding.tvFragmentViewProfileNoUserFavouriteTagsToDisplay.gone()
                        binding.cgFragmentViewProfileUserFavouriteTags.removeAllViews()

                        res.body.results.forEach { tag ->
                            val chip = Chip(requireContext())
                            chip.text = tag.name
                            chip.chipBackgroundColor =
                                ColorStateList.valueOf(Color.parseColor("#4fb595"))
                            binding.cgFragmentViewProfileUserFavouriteTags.addView(chip)

                        }

                    }
                }
            }
        }
    }

}