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
import com.minor_project.flaamandroid.adapters.PostDiscussionAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.CreateDiscussionRequest
import com.minor_project.flaamandroid.data.request.PostCommentRequest
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.databinding.FragmentPostDiscussionBinding
import com.minor_project.flaamandroid.databinding.LayoutAddDiscussionBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.FieldPosition
import javax.inject.Inject

@AndroidEntryPoint
class PostDiscussionFragment(ideaId: Int) : Fragment() {

    private lateinit var postDiscussionAdapter: PostDiscussionAdapter

    private lateinit var binding: FragmentPostDiscussionBinding

    private var discussionsList: ArrayList<DiscussionsResponse.Result> = ArrayList()

    private val viewModel: PostDiscussionViewModel by viewModels()

    private val mIdeaId = ideaId

    private var positonOfDiscussion = 0

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var popupAddDiscussionBinding: LayoutAddDiscussionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDiscussionBinding.inflate(inflater)

        postDiscussionAdapter = PostDiscussionAdapter(this, requireContext(), discussionsList)
        binding.rvDiscussions.setHasFixedSize(true)

        binding.rvDiscussions.adapter = postDiscussionAdapter

        initObservers()
        initOnClick()
        return binding.root
    }

    private fun initObservers() {
        postDiscussionAdapter.setToList(arrayListOf())
        viewModel.getDiscussions(mIdeaId.toString())
        viewModel.getUserProfile()


        viewModel.comments.observe(viewLifecycleOwner){
            when(it){
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> postDiscussionAdapter.updateCommentsForDiscussion(it.body.results ?: emptyList(), positonOfDiscussion)
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch User Profile!")
                }

                is ApiResponse.Success -> {
                }
            }
        }


        viewModel.createDiscussion.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiResponse.Success -> {
                    viewModel.getDiscussions(mIdeaId.toString())
                    makeToast("Discussion Created Successfully!")
                }
            }
        }


        viewModel.getDiscussions.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    if (it.body.results.isNullOrEmpty()) {
                        binding.tvNoDiscussionsAdded.visibility = View.VISIBLE
                        binding.rvDiscussions.visibility = View.GONE
                    } else {
                        binding.tvNoDiscussionsAdded.visibility = View.GONE
                        binding.rvDiscussions.visibility = View.VISIBLE

                        postDiscussionAdapter.setToList(arrayListOf())

                        postDiscussionAdapter.addToList(it.body.results as ArrayList<DiscussionsResponse.Result>)
                    }

                }
            }
        }

        viewModel.postCommentResult.observe(viewLifecycleOwner){ it ->
            when(it){
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> makeToast("posted!!")
            }
        }


        viewModel.voteDiscussion.observe(viewLifecycleOwner) {

            when (it) {
                0 -> {
                }
                -1 -> {
                    makeToast("Discussion Successfully DownVoted!")
                }
                1 -> {
                    makeToast("Discussion Successfully UpVoted!")
                }
            }
            viewModel.getDiscussions(mIdeaId.toString())


        }


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
                viewModel.createDiscussion(
                    CreateDiscussionRequest(
                        popupAddDiscussionBinding.etAddDiscussionBody.text.toString(),
                        true,
                        mIdeaId,
                        popupAddDiscussionBinding.etAddDiscussionTitle.text.toString()
                    )
                )
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


    fun voteDiscussion(discussionId: Int, value: Int) {
        viewModel.voteDiscussion(discussionId.toString(), value)
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

    fun getComments(id: Int, position: Int){
        positonOfDiscussion = position
        viewModel.getComments(id)
    }

    fun postComment(body: PostCommentRequest, position: Int){
        positonOfDiscussion = position
        viewModel.postComment(body)
    }
}