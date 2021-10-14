package com.minor_project.flaamandroid.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.R
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

    private fun initClick() {
        binding.apply {

            civUserProfileUserImage.setOnClickListener {
                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment())
            }

            ivMenuUserProfile.setOnClickListener {
                val menuPopup = PopupMenu(requireContext(), it)
                menuPopup.menu.add("Log Out!")
                menuPopup.menu.add("Reset Password")

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
                        tvUserProfileFnameLname.text = resources.getString(
                            R.string.full_name,
                            it.body.firstName.toString(),
                            it.body.lastName.toString()
                        )

                        tvUserProfileDoj.text = resources.getString(
                            R.string.date_joined_days_ago,
                            it.body.dateJoined.toString().getDaysDiff().toString()
                        )

                        civUserProfileUserImage.loadImage(it.body.avatar.toString())
                    }
                }
            }
        }
    }

}


