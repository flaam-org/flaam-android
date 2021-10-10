package com.minor_project.flaamandroid.ui.feed.userprofile

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.databinding.FragmentEditProfileBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.gone
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.minor_project.flaamandroid.adapters.UserTagsAdapter
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var userTagsAdapter: UserTagsAdapter

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditProfileBinding

    private var userTagsListIds: ArrayList<Int>? = ArrayList()
    private var userTagsObjectList: ArrayList<TagsResponse.Result>? = ArrayList()
    private lateinit var allTagsResponse: TagsResponse


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)

        val mToolbar = binding.toolbarEditProfileFragment

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(mToolbar)
            (activity as AppCompatActivity).supportActionBar?.setTitle(com.minor_project.flaamandroid.R.string.edit_profile_toolbar_title)
        }
        mToolbar.setTitleTextColor(resources.getColor(R.color.white))
        mToolbar.setNavigationIcon(com.minor_project.flaamandroid.R.drawable.ic_arrow_back_24dp)
        mToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        initRecView()
        initObserver()
        initOnClick()

        return binding.root
    }


    private fun initOnClick() {

        var timer = Timer()
        val delay = 800L

        binding.apply {
            btnUpdateEditProfile.setOnClickListener {
                updateUserProfile()
                findNavController().popBackStack()
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
        viewModel.getUserProfile()

        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {

                    binding.apply {
                        etUsernameEditProfile.setText(it.body.username.toString())
                        etFnameEditProfile.setText(it.body.firstName.toString())
                        etLnameEditProfile.setText(it.body.lastName.toString())
                        etEmailEditProfile.setText(it.body.email.toString())

                        viewModel.getTagsForId(it.body.favouriteTags)
                    }

                    userTagsListIds = it.body.favouriteTags as ArrayList<Int>?


                }
            }
        }




        viewModel.tagsListFromIds.observe(viewLifecycleOwner)
        {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch User Tags!")
                }
                is ApiResponse.Success -> {

                    userTagsObjectList =
                        it.body.results as ArrayList<TagsResponse.Result>?

                    if (userTagsObjectList.isNullOrEmpty()) {
                        binding.tvNoTagsToDisplayEditProfile.visible()
                        binding.rvEditProfileTags.gone()
                    } else {
                        binding.tvNoTagsToDisplayEditProfile.gone()
                        binding.rvEditProfileTags.visible()

                        Timber.e("userTagsListNames: $userTagsObjectList")

                        userTagsAdapter.updateUserTagsList(userTagsObjectList!!)

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
                        viewModel.getTagsForId(it.body.favouriteTags)
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
                }
            }
        }


        viewModel.addTagToUsersFavouriteTags.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to Add Tag to Favourite Tags!")
                }

                is ApiResponse.Success -> {
                    makeToast("Tag Successfully Added to Favourite Tags!")
                }
            }
        }


        viewModel.removeTagFromUsersFavouriteTags.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to Remove Tag from Favourite Tags!")
                }

                is ApiResponse.Success -> {
                    makeToast("Tag Successfully Removed to Favourite Tags!")
                }
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


    private fun initRecView() {
        binding.rvEditProfileTags.layoutManager = LinearLayoutManager(this.requireContext())

        binding.rvEditProfileTags.setHasFixedSize(true)

        userTagsAdapter = UserTagsAdapter(this, requireContext(), userTagsObjectList!!)


        binding.rvEditProfileTags.adapter = userTagsAdapter
    }

    fun addToFavouriteTags(id: Int) {
        viewModel.addTagToUsersFavouriteTags(id.toString())
    }

    fun removeFromFavouriteTags(id: Int) {
        viewModel.removeTagFromUsersFavouriteTags(id.toString())
    }
}