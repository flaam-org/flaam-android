package com.minor_project.flaamandroid.ui.userprofile.tabs

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.adapters.DragManageAdapter
import com.minor_project.flaamandroid.adapters.MilestonesAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.CreateUpdateIdeaRequest
import com.minor_project.flaamandroid.data.request.TagsRequest
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentEditIdeaBinding
import com.minor_project.flaamandroid.databinding.LayoutAddEditTagsBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class EditIdeaFragment : Fragment() {

    private var milestonesList: ArrayList<String> = ArrayList()

    private var userTagsList: ArrayList<Int> = ArrayList()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditIdeaBinding
    private val viewModel: EditIdeaViewModel by viewModels()

    private val args: EditIdeaFragmentArgs by navArgs()

    private lateinit var adapter: MilestonesAdapter
    private lateinit var popupBinding: LayoutAddEditTagsBinding

    private lateinit var allTagsResponse: TagsResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditIdeaBinding.inflate(inflater)

        adapter = MilestonesAdapter(this, requireContext(), milestonesList)
        binding.rvMilestonesEditIdea.adapter = adapter

        // Setup ItemTouchHelper
        val callback = DragManageAdapter(
            adapter,
            requireContext(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        )

        val helper = ItemTouchHelper(callback)

        helper.attachToRecyclerView(binding.rvMilestonesEditIdea)


        initObservers()
        initOnClick()

        return binding.root
    }

    private fun initOnClick() {
        binding.apply {

            btnEditIdea.setOnClickListener {
                if (validate()) {
                    viewModel.updateIdea(
                        CreateUpdateIdeaRequest(
                            null,
                            binding.etEditIdeaBody.text.toString(),
                            binding.etEditIdeaDescription.text.toString(),
                            true,
                            milestonesList,
                            userTagsList.distinct(),
                            binding.etEditIdeaTitle.text.toString()
                        ), args.ideaId
                    )
                } else {
                    makeToast("Missing Required Fields!")
                }
            }
            ivEditIdeaClose.setOnClickListener {
                findNavController().popBackStack()
            }


            btnAddMilestoneEditIdea.setOnClickListener {
                val milestoneName = binding.etAddMilestoneEditIdea.text.toString()
                if (milestoneName.isEmpty()) {
                    makeToast("Enter a Milestone to Add!")
                } else {
                    addMilestone(milestoneName)
                    binding.etAddMilestoneEditIdea.setText("")
                    binding.root.makeSnackBar("Added $milestoneName")
                }

                hideKeyboard()
            }

        }
    }

    private fun initObservers() {
        requireContext().showProgressDialog()
        viewModel.finalTagsList(userTagsList)
        viewModel.getIdeaDetails(args.ideaId)
        viewModel.ideaDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    requireContext().hideProgressDialog()
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    requireContext().hideProgressDialog()
                    binding.apply {
                        etEditIdeaTitle.setText(it.body.title.toString())

                        it.body.tags!!.forEach { tag ->
                            val chip = Chip(requireContext())
                            chip.text = tag.name
                            chip.chipBackgroundColor =
                                ColorStateList.valueOf(Color.parseColor("#4fb595"))
                            chip.isCloseIconVisible = true
                            chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
                            chip.setOnCloseIconClickListener {
                                userTagsList.remove(tag.id)
                                chip.gone()
                                updateTagsList()
                            }
                            binding.chipGroupEditIdeaTags.addView(chip)
                            userTagsList.add(tag.id!!)

                        }

                        etEditIdeaDescription.setText(it.body.description.toString())
                        etEditIdeaBody.setText(it.body.body.toString())

                        it.body.milestones!!.forEach { milestone ->
                            milestonesList.add(milestone[1])
                        }
                        adapter.notifyDataSetChanged()

                        lifecycleScope.launch(Dispatchers.Main) {
                            binding.civEditIdeaUserImage.loadSVG(it.body.ownerAvatar.toString())
                        }
                    }
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
                        chipEditIdeaAddTag.setOnClickListener {
                            showAddEditTagPopup()
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
                    binding.chipGroupEditIdeaTags.addView(chip)

                    userTagsList.add(res.body.id!!)

                    updateTagsList()
                    makeToast("Tag Created!")
                }
            }
        }

        viewModel.updateIdea.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.body.toString())
                }
                is ApiResponse.Success -> {
                    makeToast("Idea Edited Successfully!")
                    findNavController().popBackStack()
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
            chip.isCloseIconVisible = true
            chip.closeIconTint = ColorStateList.valueOf(Color.parseColor("#F75D59"))
            chip.setOnCloseIconClickListener {
                userTagsList.remove(data.results.first {
                    it.name == chip.text
                }.id!!)
                updateTagsList()
                chip.gone()
            }
            binding.chipGroupEditIdeaTags.addView(chip)

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

    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if (etEditIdeaTitle.text.isNullOrEmpty()) {
                etEditIdeaTitle.error = emptyFieldError
                return false
            }

            if (chipGroupEditIdeaTags.childCount <= 1) {
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

}