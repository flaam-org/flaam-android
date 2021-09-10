package com.minor_project.flaamandroid.feed.userprofile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.MainActivity
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentUserProfileBinding
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.getDaysDiff
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.google.android.material.tabs.TabLayout

import com.google.android.material.tabs.TabLayoutMediator
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.utils.gone


@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    var userTagsList: ArrayList<Int>? = ArrayList()

    private val viewModel: UserProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentUserProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserProfileBinding.inflate(inflater)


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
                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToMyProfileFragment())
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

//    private fun openAddEditTagsDialog() {
//
//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.layout_add_edit_tags)
//        val createBtn = dialog.findViewById(R.id.btn_create_tag) as AppCompatTextView
//        val cancelBtn = dialog.findViewById(R.id.btn_cancel_tag) as AppCompatTextView
//        createBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        cancelBtn.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }


    private fun showTagsMenuPopup(data: TagsResponse) {
        val menuPopup = PopupMenu(requireContext(), binding.includeAddEditTags.etAddSelectTag)


        for (tag in data.tagsResponseItems!!) {
            menuPopup.menu.add(tag.name.toString())
        }


        menuPopup.setOnMenuItemClickListener { menuItem ->

            if (binding.chipGroupTags.size < 6) {
                val chip = Chip(requireContext())
                chip.text = menuItem.title
                binding.chipGroupTags.addView(chip)
                chip.isCloseIconVisible = true
                chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                chip.setOnCloseIconClickListener {
                    userTagsList?.remove(data.tagsResponseItems.first {
                        it.name == chip.text
                    }.id!!)
                    updateTags()
                    chip.gone()
                }

                userTagsList!!.add(data.tagsResponseItems.first {
                    it.name == chip.text
                }.id!!)

                updateTags()

                makeToast("" + data.tagsResponseItems.first {
                    it.name == chip.text
                }.id!!)
            } else {
                binding.includeAddEditTags.root.visibility = View.GONE
                binding.chipEdit.isClickable = false
            }

            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    fun initObservers() {
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
        viewModel.getUserProfile()
        viewModel.tagsListFromIds.observe(viewLifecycleOwner)
        {
            when (it) {
                is ApiException.Error -> {
                    makeToast("error")
                }

                is ApiException.Success -> {
                    for (tag in it.body.tagsResponseItems!!) {
                        val chip = Chip(requireContext())
                        chip.text = tag.name
                        chip.isCloseIconVisible = true

                        chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                        chip.setOnCloseIconClickListener {
                            chip.gone()
                            userTagsList?.remove(tag.id)
                            updateTags()
                        }
                        binding.chipGroupTags.addView(chip)
                    }

                }
            }
        }
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiException.Success -> {

                    binding.apply {
                        tvUserProfileFnameLname.text =
                            it.body.firstName.toString() + " " + it.body.lastName.toString()
                        tvUserProfileDoj.text =
                            it.body.dateJoined.toString().getDaysDiff().toString() + " days ago"

                        if (!it.body.favouriteTags.isNullOrEmpty() && userTagsList?.isEmpty() == true) {
                            userTagsList?.addAll(it.body.favouriteTags!!)
                            viewModel.getTagsForId(it.body.favouriteTags as List<Int>?)
                        }
                        if(userTagsList?.isEmpty() == true){
                                makeToast("No Tags Added in your Profile, Add Tags!")
                        }

                    }


                }
            }
        }

        viewModel.getTags()
        viewModel.tagsList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to fetch tags!")
                }

                is ApiException.Success -> {

                    binding.apply {

                        val allTagsResponse = it.body
                        chipEdit.setOnClickListener {
                            binding.includeAddEditTags.root.visibility = View.VISIBLE
                            showTagsMenuPopup(allTagsResponse)
                        }
                    }


                }
            }
        }

        var timer = Timer()
        val DELAY = 800L

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


        viewModel.tagsListFiltered.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> makeToast(it.message.toString())
                is ApiException.Success -> showTagsMenuPopup(it.body)
            }
        }



        viewModel.updateUserProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> {
                    makeToast("Unable to Update Tags!")
                }

                is ApiException.Success -> {
                    makeToast("Profile Updated!")
                }
            }
        }


        viewModel.createNewTag.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiException.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiException.Success -> {
                    if (binding.chipGroupTags.size < 6) {
                        val chip = Chip(requireContext())
                        chip.text = binding.includeAddEditTags.etAddSelectTag.text.toString()
                        chip.isCloseIconVisible = true
                        chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                        chip.setOnCloseIconClickListener {
                            userTagsList?.remove(res.body.id!!)
                            chip.gone()
                            updateTags()
                        }
                        binding.chipGroupTags.addView(chip)

                        userTagsList!!.add(res.body.id!!)

                        updateTags()

                    } else {
                        makeToast("You cannot add more than 5 Tags!")
                        binding.includeAddEditTags.root.visibility = View.GONE
                        binding.chipEdit.isClickable = false
                    }
                    makeToast("Tag Created!")
                }
            }
        }
    }

    private fun updateTags(){
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

}


