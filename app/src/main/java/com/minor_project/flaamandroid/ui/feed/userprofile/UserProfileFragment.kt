package com.minor_project.flaamandroid.ui.feed.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.activities.MainActivity
import com.minor_project.flaamandroid.adapters.UserProfileViewPagerAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.FragmentUserProfileBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentUserProfileBinding


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
                0 -> tab.text = "My Bookmarks"
                1 -> tab.text = "My Ideas"
                2 -> tab.text = "My Implementations"
            }
        }.attach()


        initClick()

        return binding.root
    }

    private fun initClick() {
        binding.apply {

            civUserProfileUserImage.setOnClickListener {
                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment())
            }

            ivMenuUserProfile.setOnClickListener {
                val menuPopup = PopupMenu(requireContext(), it)
                menuPopup.menu.add("Log Out!")

                menuPopup.setOnMenuItemClickListener {

                    if (it.title == "Log Out!") {
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

                    return@setOnMenuItemClickListener true
                }

                menuPopup.show()
            }

        }
    }

    private fun initObservers() {

        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {

                    binding.apply {
                        tvUserProfileFnameLname.text =
                            it.body.firstName.toString() + " " + it.body.lastName.toString()

                        tvUserProfileDoj.text =
                            it.body.dateJoined.toString().getDaysDiff().toString() + " days ago"

                        civUserProfileUserImage.loadImage(it.body.avatar.toString())
                    }
                }
            }
        }
    }

}


