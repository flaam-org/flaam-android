package com.minor_project.flaamandroid.ui.userprofile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.activities.MainActivity
import com.minor_project.flaamandroid.adapters.UserProfileViewPagerAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.FragmentUserProfileBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentUserProfileBinding

    private val args: UserProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserProfileBinding.inflate(inflater)

        initObservers()

        val tabLayout = binding.tabLayoutUserProfile


        val adapter = UserProfileViewPagerAdapter(this)
        binding.viewPagerUserProfile.adapter = adapter


        TabLayoutMediator(
            tabLayout, binding.viewPagerUserProfile
        ) { tab, position ->
            when (position) {
                0 -> tab.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_bookmarks_24dp)
                1 -> tab.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_ideas_24dp)
                2 -> tab.icon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_my_implementations_24dp
                )
            }
        }.attach()


        initClick()

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

    private fun initClick() {
        binding.apply {

            civUserProfileUserImage.setOnClickListener {
                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment())
            }

            ivMenuUserProfile.setOnClickListener { res ->
                val menuPopup = PopupMenu(requireContext(), res)
                menuPopup.menu.add("Log Out!")
                menuPopup.menu.add("Reset Password")
                menuPopup.menu.add("Edit Profile!")

                menuPopup.setOnMenuItemClickListener {


                    when (it.title) {
                        "Log Out!" -> {
                            runBlocking {
                                preferences.logoutUser()
                            }
                            requireContext().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )

                            activity?.finish()

                        }

                        "Reset Password" -> {

                        }
                        "Edit Profile!" -> {
                            findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment())
                        }
                    }


                    return@setOnMenuItemClickListener true
                }

                menuPopup.show()
            }

        }

        binding.includeViewProfileLayout.apply {
            ivBackViewProfile.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObservers() {
        val shimmerLayoutUserProfileOwner = binding.shimmerLayout
        val shimmerLayoutViewProfile = binding.includeViewProfileLayout.shimmerLayout
        if (args.username != null) {
            binding.includeViewProfileLayout.root.visible()
            binding.llUserProfileOwner.gone()
            shimmerLayoutViewProfile.startShimmer()
            viewModel.getUserProfileFromUsername(args.username!!)
        } else {
            binding.includeViewProfileLayout.root.gone()
            binding.llUserProfileOwner.visible()
            shimmerLayoutUserProfileOwner.startShimmer()
            viewModel.getUserProfile()
        }

        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {

                    shimmerLayoutUserProfileOwner.stopShimmer()
                    shimmerLayoutUserProfileOwner.gone()
                    binding.llUserProfileUserDetails.visible()

                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {

                    shimmerLayoutUserProfileOwner.stopShimmer()
                    shimmerLayoutUserProfileOwner.gone()
                    binding.llUserProfileUserDetails.visible()

                    binding.apply {
                        tvUserProfileFnameLname.text = resources.getString(
                            R.string.full_name,
                            it.body.firstName.toString(),
                            it.body.lastName.toString()
                        )

                        tvUserProfileDoj.text = resources.getString(
                            R.string.date_joined_days_ago,
                            it.body.dateJoined.toString().getDaysDiff().toString()
                        )

                        lifecycleScope.launch(Dispatchers.IO) {
                            civUserProfileUserImage.loadSVG(it.body.avatar.toString())
                        }
                    }

                }
            }
        }


        viewModel.publicUserProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    shimmerLayoutViewProfile.stopShimmer()
                    shimmerLayoutViewProfile.gone()
                    binding.includeViewProfileLayout.llViewProfile.visible()

                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {
                    shimmerLayoutViewProfile.stopShimmer()
                    shimmerLayoutViewProfile.gone()
                    binding.includeViewProfileLayout.llViewProfile.visible()

                    binding.includeViewProfileLayout.apply {
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
//                    shimmerLayout.stopShimmer()
//                    shimmerLayout.visibility = View.GONE
                    makeToast("Unable to fetch Favourite Tags!")
                }
                is ApiResponse.Success -> {
//                    shimmerLayout.stopShimmer()
//                    shimmerLayout.visibility = View.GONE
                    if (res.body.results.isNullOrEmpty()) {
                        binding.includeViewProfileLayout.tvFragmentViewProfileNoUserFavouriteTagsToDisplay.visible()

                    } else {
                        binding.includeViewProfileLayout.tvFragmentViewProfileNoUserFavouriteTagsToDisplay.gone()
                        binding.includeViewProfileLayout.cgFragmentViewProfileUserFavouriteTags.removeAllViews()

                        res.body.results.forEach { tag ->
                            val chip = Chip(requireContext())
                            chip.text = tag.name
                            chip.chipBackgroundColor =
                                ColorStateList.valueOf(Color.parseColor("#4fb595"))
                            binding.includeViewProfileLayout.cgFragmentViewProfileUserFavouriteTags.addView(
                                chip
                            )

                        }

                    }
                }
            }
        }
    }

}


