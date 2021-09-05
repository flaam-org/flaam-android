package com.minor_project.flaamandroid.feed.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.databinding.FragmentUserProfileBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: MyProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentUserProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserProfileBinding.inflate(inflater)

        val tabLayout = binding.tabLayoutUserProfile
        tabLayout.addTab(tabLayout.newTab().setText("My Bookmarks"))
        tabLayout.addTab(tabLayout.newTab().setText("My Ideas"))
        tabLayout.addTab(tabLayout.newTab().setText("My Implementations"))

        val adapter = UserProfileViewPagerAdapter(childFragmentManager, tabLayout.tabCount)
        binding.viewPagerUserProfile.adapter = adapter
        binding.viewPagerUserProfile.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPagerUserProfile.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.apply {

            fabUserProfileEditProfile.setOnClickListener {
                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToMyProfileFragment())
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserProfile()

        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiException.Success -> {
                    runBlocking {
                        binding.apply {
                            tvUserProfileFnameLname.text =
                                it.body.firstName.toString() + " " + it.body.lastName.toString()
                            tvUserProfileDoj.text = (it.body.dateJoined.toString())
                        }
                    }

                }
            }
        }
    }


}


