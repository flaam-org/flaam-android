package com.minor_project.flaamandroid.feed.userprofile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.databinding.FragmentEditProfileBinding
import com.minor_project.flaamandroid.feed.FeedPostAdapter
import com.minor_project.flaamandroid.models.FeedPostModel
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.gone
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditProfileBinding

    private var userTagsList: ArrayList<Int>? = ArrayList()
    private var userTagsListNames: ArrayList<String>? = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)
        initObserver()
        initOnClick()

        return binding.root
    }

    private fun updateUserProfile() {
        viewModel.updateUserProfile(
            UpdateProfileRequest(
                null,
                null,
                null,
                binding.etFnameEditProfile.text.toString(),
                binding.etLnameEditProfile.text.toString(),
                null,
                null,
                null
            )
        )
    }

    private fun initOnClick() {
        binding.apply {
            btnUpdateEditProfile.setOnClickListener {
                updateUserProfile()
            }
        }

    }

    private fun initObserver() {
        viewModel.getUserProfile()

        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiException.Success -> {
                    runBlocking {
                        binding.apply {
                            etUsernameEditProfile.setText(it.body.username.toString())
                            etFnameEditProfile.setText(it.body.firstName.toString())
                            etLnameEditProfile.setText(it.body.lastName.toString())
                            etEmailEditProfile.setText(it.body.email.toString())
                            userTagsList!!.addAll(it.body.favouriteTags!!)
                            viewModel.getTagsForId(it.body.favouriteTags as List<Int>?)
                        }
                    }

                }
            }
        }




        viewModel.tagsListFromIds.observe(viewLifecycleOwner)
        {
            when (it) {
                is ApiException.Error -> {
                    makeToast("error")
                }

                is ApiException.Success -> {
                    for (tag in it.body.tagsResponseItems!!) {
                        userTagsListNames!!.add(tag.name!!)
                    }

                    if (userTagsList.isNullOrEmpty()) {
                        binding.tvNoTagsToDisplayEditProfile.visibility = View.VISIBLE
                        binding.rvEditProfileTags.visibility = View.GONE
                    } else {
                        binding.tvNoTagsToDisplayEditProfile.visibility = View.GONE
                        binding.rvEditProfileTags.visibility = View.VISIBLE


//                        val userTagsList = ArrayList<FeedPostModel>()

                        binding.rvEditProfileTags.layoutManager = GridLayoutManager(context, 2)

                        binding.rvEditProfileTags.setHasFixedSize(true)


                        val userTagsAdapter = UserTagsAdapter(requireContext(), userTagsListNames!!)
                        binding.rvEditProfileTags.adapter = userTagsAdapter
                    }

                }
            }
        }







        viewModel.updateUserProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to Update Data")
                }

                is ApiException.Success -> {
                    makeToast("Updated Profile Successfully")
                    findNavController().popBackStack()
                }
            }
        }


    }


}