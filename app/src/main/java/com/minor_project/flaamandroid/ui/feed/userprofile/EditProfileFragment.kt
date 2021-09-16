package com.minor_project.flaamandroid.ui.feed.userprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentEditProfileBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var userTagsAdapter: UserTagsAdapter

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditProfileBinding

    private var userTagsList: ArrayList<Int>? = ArrayList()
    private var userTagsListNames: ArrayList<String>? = ArrayList()
    private lateinit var allTagsResponse: TagsResponse


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)

        populateTagsInRecyclerView()
        initObserver()
        initOnClick()

        return binding.root
    }


    private fun initOnClick() {

        var timer = Timer()
        val DELAY = 800L

        binding.apply {
            btnUpdateEditProfile.setOnClickListener {
                updateUserProfile()
                findNavController().popBackStack()
            }
        }

        binding.btnAddTagsEditProfile.setOnClickListener {
            showAddEditTagPopup()
            userTagsAdapter.updateUserTagsList(userTagsListNames!!)
            populateTagsInRecyclerView()
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

                    }, DELAY
                )

            }

        })



        binding.includeAddEditTags.btnCreateTag.setOnClickListener {
            if (validate()) {
                viewModel.createNewTag(
                    TagsRequest(null, binding.includeAddEditTags.etAddSelectTag.text.toString())
                )

                userTagsAdapter.updateUserTagsList(userTagsListNames!!)
                populateTagsInRecyclerView()
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
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch User Tags!")
                }
                is ApiResponse.Success -> {
                    for (tag in it.body.tagsResponseItems!!) {
                        userTagsListNames!!.add(tag.name!!)
                    }

                    if (userTagsList.isNullOrEmpty()) {
                        binding.tvNoTagsToDisplayEditProfile.visibility = View.VISIBLE
                        binding.rvEditProfileTags.visibility = View.GONE
                    } else {
                        binding.tvNoTagsToDisplayEditProfile.visibility = View.GONE
                        binding.rvEditProfileTags.visibility = View.VISIBLE

                        userTagsAdapter.updateUserTagsList(userTagsListNames!!)
                        populateTagsInRecyclerView()
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
                    userTagsList!!.add(res.body.id!!)
                    updateTags()
                    userTagsAdapter.updateUserTagsList(userTagsListNames!!)
                    populateTagsInRecyclerView()
                    makeToast("Tag Created!")
                }
            }
        }


    }

    private fun showAddEditTagPopup() {
        binding.includeAddEditTags.root.visibility = View.VISIBLE
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


        for (tag in data.tagsResponseItems!!) {
            menuPopup.menu.add(tag.name.toString())
        }


        menuPopup.setOnMenuItemClickListener { menuItem ->
            val selectedTag = menuItem.title
            userTagsList!!.add(data.tagsResponseItems.first {
                it.name == selectedTag
            }.id!!)

            updateTags()
            userTagsAdapter.updateUserTagsList(userTagsListNames!!)
            populateTagsInRecyclerView()

            makeToast("" + data.tagsResponseItems.first {
                it.name == selectedTag
            }.id!!)

            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
    }

    private fun updateTags() {

        viewModel.updateUserProfile(
            UpdateProfileRequest(
                null,
                null,
                userTagsList,
                null,
                null,
                null,
                null,
                null
            )
        )
    }

    private fun updateUserProfile() {
        viewModel.updateUserProfile(
            UpdateProfileRequest(
                null,
                null,
                userTagsList,
                binding.etFnameEditProfile.text.toString(),
                binding.etLnameEditProfile.text.toString(),
                null,
                null,
                null
            )
        )
    }


    private fun populateTagsInRecyclerView() {
        binding.rvEditProfileTags.layoutManager = GridLayoutManager(context, 2)

        binding.rvEditProfileTags.setHasFixedSize(true)

        userTagsAdapter = UserTagsAdapter(requireContext(), userTagsListNames!!)
        binding.rvEditProfileTags.adapter = userTagsAdapter
    }


}