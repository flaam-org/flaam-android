package com.minor_project.flaamandroid.ui.feed.post.tabs

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.FragmentPostDiscussionBinding
import com.minor_project.flaamandroid.databinding.LayoutAddDiscussionBinding
import com.minor_project.flaamandroid.databinding.LayoutDiscussionItemBinding
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.showPopupDimBehind
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostDiscussionFragment(ideaId: Int) : Fragment() {

    private lateinit var binding: FragmentPostDiscussionBinding

    private val viewModel: PostDiscussionViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var popupAddDiscussionBinding: LayoutAddDiscussionBinding
    private lateinit var newDiscussionBinding: LayoutDiscussionItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDiscussionBinding.inflate(inflater)
        initOnClick()
        return binding.root
    }

    private fun initOnClick() {
        binding.apply {
            btnAddDiscussion.setOnClickListener {
                showAddDiscussionPopUp()
            }
        }
    }

    private fun showAddDiscussionPopUp() {


        popupAddDiscussionBinding = LayoutAddDiscussionBinding.inflate(layoutInflater)

        val popup = PopupWindow(
            popupAddDiscussionBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            false
        )


        popupAddDiscussionBinding.btnAddDiscussion.setOnClickListener {

            if (validateCreateDiscussion()) {
                newDiscussionBinding = LayoutDiscussionItemBinding.inflate(layoutInflater)
                newDiscussionBinding.apply {
                    tvDiscussionTitle.text =
                        popupAddDiscussionBinding.etAddDiscussionTitle.text.toString()
                    tvDiscussionBody.text =
                        popupAddDiscussionBinding.etAddDiscussionBody.text.toString()
                }
                val newDiscussionView = newDiscussionBinding.root
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(8, 8, 8, 8)
                newDiscussionView.layoutParams = lp
                binding.llPostDiscussions.addView(newDiscussionView)
                //todo add the network call to create new discussion
                popup.dismiss()

            } else {

                makeToast("Missing Required Fields!")

            }
        }


        popupAddDiscussionBinding.btnCancelDiscussion.setOnClickListener {
            popup.dismiss()
        }


        popup.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        popup.showPopupDimBehind()
    }

    private fun validateCreateDiscussion(): Boolean {

        val emptyFieldError = "This Field Can't Be Empty!"
        popupAddDiscussionBinding.apply {
            if (etAddDiscussionTitle.text.isNullOrEmpty()) {
                tilAddDiscussionTitle.error = emptyFieldError
                return false
            }
            return true
        }

    }
}