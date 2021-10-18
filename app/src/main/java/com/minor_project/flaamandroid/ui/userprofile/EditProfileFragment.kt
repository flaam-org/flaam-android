package com.minor_project.flaamandroid.ui.userprofile

import android.app.ActionBar
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.minor_project.flaamandroid.adapters.UserFavouriteTagsAdapter
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.LayoutAddEditTagsBinding
import com.minor_project.flaamandroid.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels()

    private val userFavouriteTagsList: ArrayList<TagsResponse.Result> = ArrayList()


    private lateinit var allTagsResponse: TagsResponse
    private lateinit var popupBinding: LayoutAddEditTagsBinding

    private var updateProfile = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)





        binding.ivBackEditProfile.setOnClickListener {
            findNavController().popBackStack()
        }



        initObserver()
        initOnClick()

        return binding.root
    }


    private fun initOnClick() {


        binding.apply {
            btnUpdateEditProfile.setOnClickListener {
                updateProfile = true
                updateUserProfile()
            }
        }

        binding.btnAddTagsEditProfile.setOnClickListener {
            showAddEditTagPopup()

        }


    }

    private fun initObserver() {

        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch your Profile!")
                }

                is ApiResponse.Success -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.civEditProfileUserImage.loadSVG(it.body.avatar.toString())
                    }

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
        { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Favourite Tags!")
                }
                is ApiResponse.Success -> {
                    if (res.body.results.isNullOrEmpty()) {
                        binding.tvEditProfileNoUserFavouriteTagsToDisplay.visibility = View.VISIBLE

                    } else {
                        binding.tvEditProfileNoUserFavouriteTagsToDisplay.visibility = View.GONE




                        binding.cgEditProfileUserFavouriteTags.removeAllViews()

                        res.body.results.forEach { tag ->
                            val chip = Chip(requireContext())
                            chip.text = tag.name
                            chip.chipBackgroundColor =
                                ColorStateList.valueOf(Color.parseColor("#4fb595"))
                            chip.isCloseIconVisible = true

                            chip.setOnCloseIconClickListener {
                                tag.id?.also {
                                    removeFromFavouriteTags(it)
                                }
                            }
                            binding.cgEditProfileUserFavouriteTags.addView(chip)

                        }

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

        popupBinding = LayoutAddEditTagsBinding.inflate(layoutInflater)

        val popup = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            false
        )


        popupBinding.btnCreateTag.setOnClickListener {
            if (validate()) {
                viewModel.createNewTag(
                    TagsRequest(null, popupBinding.etAddSelectTag.text.toString())
                )

            } else {
                makeToast("Missing Required Fields!")
            }
        }

        var timer = Timer()
        val delay = 800L

        popupBinding.etAddSelectTag.addTextChangedListener(object : TextWatcher {

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

        popupBinding.btnCancelTag.setOnClickListener {
            popup.dismiss()
        }


        popup.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        popup.showPopupDimBehind()



        showTagsMenuPopup(allTagsResponse)
    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        popupBinding.apply {
            if (etAddSelectTag.text.isNullOrEmpty()) {
                etAddSelectTag.error = emptyFieldError
                return false
            }
            return true
        }
    }


    private fun showTagsMenuPopup(data: TagsResponse) {
        val menuPopup = PopupMenu(requireContext(), popupBinding.etAddSelectTag)


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