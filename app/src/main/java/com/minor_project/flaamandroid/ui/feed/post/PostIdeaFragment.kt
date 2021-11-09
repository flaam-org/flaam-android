package com.minor_project.flaamandroid.ui.feed.post

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.adapters.DragManageAdapter
import com.minor_project.flaamandroid.adapters.MilestonesAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.CreateUpdateIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentPostIdeaBinding
import com.minor_project.flaamandroid.databinding.LayoutAddEditTagsBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PostIdeaFragment : Fragment() {

    private var milestonesList: ArrayList<String> = ArrayList()

    private var userTagsList: ArrayList<Int> = ArrayList()
    private lateinit var binding: FragmentPostIdeaBinding

    private val viewModel: PostIdeaViewModel by viewModels()

    private lateinit var adapter: MilestonesAdapter


    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var popupBinding: LayoutAddEditTagsBinding
    private lateinit var allTagsResponse: TagsResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentPostIdeaBinding.inflate(inflater)
        adapter = MilestonesAdapter(this, requireContext(), milestonesList)
        binding.rvMilestonesPostIdea.adapter = adapter

        // Setup ItemTouchHelper
        val callback = DragManageAdapter(
            adapter,
            requireContext(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        )

        val helper = ItemTouchHelper(callback)

        helper.attachToRecyclerView(binding.rvMilestonesPostIdea)

        initObservers()
        initOnClick()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
    }

    private fun initOnClick() {

        binding.apply {

            btnAddMilestonePostIdea.setOnClickListener {
                val milestoneName = binding.etAddMilestonePostIdea.text.toString()
                if (milestoneName.isEmpty()) {
                    makeToast("Enter a Milestone to Add!")
                } else {
                    addMilestone(milestoneName)
                    binding.etAddMilestonePostIdea.setText("")
                    binding.root.makeSnackBar("Added $milestoneName")
                }

                hideKeyboard()
            }

            ivPostIdeaClose.setOnClickListener {
                findNavController().popBackStack()
            }

            chipPostIdeaAddTag.setOnClickListener {
                showAddEditTagPopup()
            }

            btnPostIdea.setOnClickListener {
                if (validate()) {
                    viewModel.postIdea(
                        CreateUpdateIdeaRequest(
                            null,
                            binding.etPostIdeaBody.text.toString(),
                            binding.etPostIdeaDescription.text.toString(),
                            true,
                            milestonesList,
                            userTagsList.distinct(),
                            binding.etPostIdeaTitle.text.toString()
                        )
                    )
                } else {
                    makeToast("Missing Required Fields!")
                }
            }
        }

    }


    private fun initObservers() {
        val shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    binding.llPostIdea.visibility = View.VISIBLE
                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    binding.llPostIdea.visibility = View.VISIBLE
                    binding.apply {
                        lifecycleScope.launch(Dispatchers.Main) {
                            civPostIdeaUserImage.loadSVG(it.body.avatar.toString())
                        }
                    }
                }
            }
        }

        viewModel.finalTagsList(userTagsList)
        viewModel.postIdea.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiResponse.Success -> {
                    makeToast("Idea Posted Successfully!")
                    findNavController().popBackStack()
                }
            }
        }


        viewModel.getTags()
        viewModel.tagsList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch tags!")
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
                    val chip = Chip(requireContext())
                    chip.text = res.body.name
                    chip.chipBackgroundColor =
                        ColorStateList.valueOf(Color.parseColor("#4fb595"))
                    chip.isCloseIconVisible = true
                    chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                    chip.setOnCloseIconClickListener {
                        userTagsList.remove(res.body.id!!)
                        chip.gone()
                        updateTagsList()
                    }
                    binding.chipGroupPostIdeaTags.addView(chip)

                    userTagsList.add(res.body.id!!)

                    updateTagsList()
                    makeToast("Tag Created!")
                }
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
            if (validateCreateTag()) {
                viewModel.createNewTag(
                    TagsRequest(
                        popupBinding.etDescriptionAddSelectTag.text.toString(),
                        popupBinding.etAddSelectTag.text.toString()
                    )
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

        if (::allTagsResponse.isInitialized) {
            showTagsMenuPopup(allTagsResponse)
        }


    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if (etPostIdeaTitle.text.isNullOrEmpty()) {
                etPostIdeaTitle.error = emptyFieldError
                return false
            }

            if (chipGroupPostIdeaTags.childCount <= 1) {
                makeToast("Add At least One Tag!")
                return false
            }
            return true
        }
    }

    private fun validateCreateTag(): Boolean {
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
            val chip = Chip(requireContext())
            chip.text = menuItem.title
            chip.chipBackgroundColor =
                ColorStateList.valueOf(Color.parseColor("#4fb595"))
            binding.chipGroupPostIdeaTags.addView(chip)
            chip.isCloseIconVisible = true
            chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
            chip.setOnCloseIconClickListener {
                userTagsList.remove(data.results.first {
                    it.name == chip.text
                }.id!!)
                updateTagsList()
                chip.gone()
            }

            userTagsList.add(data.results.first {
                it.name == chip.text
            }.id!!)

            updateTagsList()

            makeToast("" + data.results.first {
                it.name == chip.text
            }.id!!)


            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
    }

    private fun updateTagsList() {
        viewModel.finalTagsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            userTagsList = (list as ArrayList<Int>?)!!
        })
    }

    private fun addMilestone(milestoneName: String) {
        milestonesList.add(milestoneName)
        adapter.notifyDataSetChanged()
    }

    fun deleteMilestone(model: String) {
        milestonesList.remove(model)
        adapter.notifyDataSetChanged()
    }

}