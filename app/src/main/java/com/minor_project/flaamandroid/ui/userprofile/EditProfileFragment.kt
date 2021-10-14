package com.minor_project.flaamandroid.ui.userprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.databinding.FragmentEditProfileBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.minor_project.flaamandroid.adapters.UserFavouriteTagsAdapter
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.utils.visible


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels()

    private val userFavouriteTagsList: ArrayList<TagsResponse.Result> = ArrayList()

    private lateinit var userFavouriteTagsAdapter: UserFavouriteTagsAdapter

    private lateinit var allTagsResponse: TagsResponse

    private var updateProfile = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)

        val mToolbar = binding.toolbarEditProfileFragment

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(mToolbar)
            (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.edit_profile_toolbar_title)
        }
        mToolbar.setTitleTextColor(resources.getColor(R.color.white))
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        mToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        userFavouriteTagsAdapter =
            UserFavouriteTagsAdapter(this, requireContext(), userFavouriteTagsList)
        binding.rvEditProfileUserFavouriteTags.setHasFixedSize(true)

        binding.rvEditProfileUserFavouriteTags.adapter = userFavouriteTagsAdapter


        initObserver()
        initOnClick()

        return binding.root
    }


    private fun initOnClick() {
        var timer = Timer()
        val delay = 800L

        binding.apply {
            btnUpdateEditProfile.setOnClickListener {
                updateProfile = true
                updateUserProfile()
            }
        }

        binding.btnAddTagsEditProfile.setOnClickListener {
            showAddEditTagPopup()

        }

        binding.includeAddEditTags.etAddSelectTag.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()

                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            viewModel.getTagsForKeyword(s.toString())
                        }

                    }, delay
                )

            }

        })



        binding.includeAddEditTags.btnCreateTag.setOnClickListener {
            if (validate()) {
                viewModel.createNewTag(
                    TagsRequest(null, binding.includeAddEditTags.etAddSelectTag.text.toString())
                )

            } else {
                makeToast("Missing Required Fields!")
            }
        }

        binding.includeAddEditTags.btnCancelTag.setOnClickListener {
            binding.includeAddEditTags.root.visibility = View.GONE
        }

    }

    private fun initObserver() {

        userFavouriteTagsAdapter.setToList(arrayListOf())
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch your Profile!")
                }

                is ApiResponse.Success -> {

                    viewModel.getUserFavouriteTags(it.body.id!!)
                    binding.apply {
                        etUsernameEditProfile.setText(it.body.username.toString())
                        etFnameEditProfile.setText(it.body.firstName.toString())
                        etLnameEditProfile.setText(it.body.lastName.toString())
                        etEmailEditProfile.setText(it.body.email.toString())
                    }

                }
            }
        }


        viewModel.userFavouriteTagsList.observe(viewLifecycleOwner)
        {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Favourite Tags!")
                }
                is ApiResponse.Success -> {
                    if (it.body.results.isNullOrEmpty()) {
                        binding.tvEditProfileNoUserFavouriteTagsToDisplay.visibility = View.VISIBLE
                        binding.rvEditProfileUserFavouriteTags.visibility = View.GONE
                    } else {
                        binding.tvEditProfileNoUserFavouriteTagsToDisplay.visibility = View.GONE
                        binding.rvEditProfileUserFavouriteTags.visibility = View.VISIBLE


                        userFavouriteTagsAdapter.setToList(arrayListOf())
                        userFavouriteTagsAdapter.addToList(it.body.results as ArrayList<TagsResponse.Result>)

                    }
                }
            }
        }


        viewModel.updateUserProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to Update Data")
                }

                is ApiResponse.Success -> {
                    makeToast("Updated Profile Successfully")
                    binding.apply {
                        etUsernameEditProfile.setText(it.body.username.toString())
                        etFnameEditProfile.setText(it.body.firstName.toString())
                        etLnameEditProfile.setText(it.body.lastName.toString())
                        etEmailEditProfile.setText(it.body.email.toString())
                        Timber.e(it.body.favouriteTags.toString() + "UserProfileUpdate")
                    }
                    viewModel.getUserFavouriteTags(it.body.id!!)
                    if (updateProfile) {
                        findNavController().popBackStack()
                        updateProfile = false
                    }
                }
            }
        }

        viewModel.getTags()
        viewModel.tagsList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Tags List!")
                }

                is ApiResponse.Success -> {

                    binding.apply {

                        allTagsResponse = it.body
                    }

                }
            }
        }



        viewModel.tagsListFiltered.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> showTagsMenuPopup(it.body)
            }
        }





        viewModel.createNewTag.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiResponse.Success -> {
                    viewModel.getTags()
                    makeToast("Tag Created!")
                    addToFavouriteTags(res.body.id!!)
                }
            }
        }


        viewModel.addTagToUsersFavouriteTags.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                makeToast("Tag Successfully Added to Favourite Tags!")
                updateUserProfile()
            } else {
                makeToast("Unable to Add Tag to Favourite Tags!")
            }
        }


        viewModel.removeTagFromUsersFavouriteTags.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                makeToast("Tag Successfully Removed to Favourite Tags!")
                updateUserProfile()
            } else {
                makeToast("Unable to Remove Tag from Favourite Tags!")
            }
        }


    }

    private fun showAddEditTagPopup() {
        binding.includeAddEditTags.root.visible()
        showTagsMenuPopup(allTagsResponse)
    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.includeAddEditTags.apply {
            if (etAddSelectTag.text.isNullOrEmpty()) {
                etAddSelectTag.error = emptyFieldError
                return false
            }
            return true
        }
    }


    private fun showTagsMenuPopup(data: TagsResponse) {
        val menuPopup = PopupMenu(requireContext(), binding.includeAddEditTags.etAddSelectTag)


        for (tag in data.results!!) {
            menuPopup.menu.add(tag.name.toString())
        }


        menuPopup.setOnMenuItemClickListener { menuItem ->
            val selectedTag = menuItem.title

            addToFavouriteTags(data.results.first {
                it.name == selectedTag
            }.id!!)

            makeToast("" + data.results.first {
                it.name == selectedTag
            }.id!!)

            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
    }


    private fun updateUserProfile() {
        viewModel.updateUserProfile(
            UpdateProfileRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                binding.etFnameEditProfile.text.toString(),
                null,
                binding.etLnameEditProfile.text.toString(),
                null,
                null,
                null
            )
        )
    }


    private fun addToFavouriteTags(id: Int) {
        viewModel.addTagToUsersFavouriteTags(id.toString())
    }

    fun removeFromFavouriteTags(id: Int) {
        viewModel.removeTagFromUsersFavouriteTags(id.toString())
    }
}