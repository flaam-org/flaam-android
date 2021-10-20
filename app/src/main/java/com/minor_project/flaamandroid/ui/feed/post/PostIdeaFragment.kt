package com.minor_project.flaamandroid.ui.feed.post

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.adapters.DragManageAdapter
import com.minor_project.flaamandroid.adapters.MilestonesAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentPostIdeaBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PostIdeaFragment : Fragment() {

    private var milestonesList: ArrayList<String> = ArrayList()

    private var userTagsList: ArrayList<Int>? = ArrayList()
    private lateinit var binding: FragmentPostIdeaBinding

    private val viewModel: PostIdeaViewModel by viewModels()

    private lateinit var adapter: MilestonesAdapter


    @Inject
    lateinit var preferences: UserPreferences

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

    private fun initOnClick() {

        binding.apply {

            btnAddMilestonePostIdea.setOnClickListener {
                val milestoneName = binding.etAddMilestonePostIdea.text.toString()
                if (milestoneName.isEmpty()) {
                    makeToast("Enter a Milestone to Add!")
                } else {
                    addMilestone(milestoneName)
                    binding.etAddMilestonePostIdea.setText("")
                    binding.root.makeSnackBar("Added ${milestoneName}")
                }

                hideKeyboard()
            }

            ivPostIdeaClose.setOnClickListener {
                findNavController().popBackStack()
            }

            btnPostIdea.setOnClickListener {
                if (validate()) {
                    viewModel.postIdea(
                        PostIdeaRequest(
                            binding.etPostIdeaBody.text.toString(),
                            binding.etPostIdeaDescription.text.toString(),
                            true,
                            milestonesList,
                            userTagsList,
                            binding.etPostIdeaTitle.text.toString()
                        )
                    )
                } else {
                    makeToast("Missing Required Fields!")
                }
            }
        }

        var timer = Timer()
        val delay = 800L

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


    private fun initObservers() {

        viewModel.finalTagsList(userTagsList!!)
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

                        val allTagsResponse = it.body
                        chipPostIdeaAddTag.setOnClickListener {
                            binding.includeAddEditTags.root.visibility = View.VISIBLE
                            showTagsMenuPopup(allTagsResponse)
                        }
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
                    chip.text = binding.includeAddEditTags.etAddSelectTag.text.toString()
                    chip.isCloseIconVisible = true
                    chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                    chip.setOnCloseIconClickListener {
                        userTagsList?.remove(res.body.id!!)
                        chip.gone()
                        updateTagsList()
                    }
                    binding.chipGroupPostIdeaTags.addView(chip)

                    userTagsList!!.add(res.body.id!!)

                    updateTagsList()
                    makeToast("Tag Created!")
                }
            }
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

    private fun showTagsMenuPopup(data: TagsResponse) {
        val menuPopup = PopupMenu(requireContext(), binding.includeAddEditTags.etAddSelectTag)


        for (tag in data.results!!) {
            menuPopup.menu.add(tag.name.toString())
        }


        menuPopup.setOnMenuItemClickListener { menuItem ->
            val chip = Chip(requireContext())
            chip.text = menuItem.title
            binding.chipGroupPostIdeaTags.addView(chip)
            chip.isCloseIconVisible = true
            chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
            chip.setOnCloseIconClickListener {
                userTagsList?.remove(data.results.first {
                    it.name == chip.text
                }.id!!)
                updateTagsList()
                chip.gone()
            }

            userTagsList!!.add(data.results.first {
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
            userTagsList = list as ArrayList<Int>?
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