package com.minor_project.flaamandroid.feed.userprofile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.databinding.FragmentUserProfileBinding
import com.minor_project.flaamandroid.databinding.LayoutAddEditTagsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.minor_project.flaamandroid.MainActivity
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.getDaysDiff
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    var chipCount : Int = 1

    private val viewModel: MyProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var bindingAddEditTags: LayoutAddEditTagsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserProfileBinding.inflate(inflater)

        bindingAddEditTags = LayoutAddEditTagsBinding.inflate(inflater)

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

        initClick()

        return binding.root
    }


    private fun initClick() {
        binding.apply {

            fabUserProfileEditProfile.setOnClickListener {
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


        menuPopup.setOnMenuItemClickListener {

            if(chipCount <=5) {
                chipCount++
                val chip = Chip(requireContext())
                chip.text = it.title

                binding.chipGroupTags.addView(chip)
            }
            else
            {
                binding.includeAddEditTags.root.visibility = View.GONE
                binding.chipEdit.isClickable = false
            }
            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
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
                            tvUserProfileDoj.text =
                                it.body.dateJoined.toString().getDaysDiff().toString() + " days ago"

                            if (it.body.favouriteTags!!.isNotEmpty()) {
                                for (tag in it.body.favouriteTags) {
                                    val chip = Chip(requireContext())
                                    chip.text = tag.toString()
                                    chipGroupTags.addView(chip)
                                }
                            } else {
                                makeToast("No Tags Added in your Profile, Add Tags!")
                            }

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
                    runBlocking {
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
        }


        binding.includeAddEditTags.etAddSelectTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getTagsForKeyword(s.toString())

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.getTagsForKeyword(s.toString())
            }

        })


        viewModel.tagsListFiltered.observe(viewLifecycleOwner) {
            when (it) {
                is ApiException.Error -> makeToast(it.message.toString())
                is ApiException.Success -> showTagsMenuPopup(it.body)
            }
        }


//        viewModel.updateUserProfile(UpdateProfileRequest(List<>, binding.etFnameMyProfile.text.toString(), binding.etLnameMyProfile.text.toString()))

        viewModel.updateUserProfile.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> {
                    makeToast("Unable to Update Data")
                }

                is ApiException.Success -> {
                    makeToast("Updated Profile Successfully")
                }
            }
        }

    }

}


