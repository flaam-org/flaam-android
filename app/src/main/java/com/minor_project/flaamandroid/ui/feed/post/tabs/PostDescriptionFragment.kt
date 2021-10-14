package com.minor_project.flaamandroid.ui.feed.post.tabs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentPostDescriptionBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDescriptionFragment(ideaId: Int) : Fragment() {

    private lateinit var binding: FragmentPostDescriptionBinding

    private val viewModel: PostDescriptionViewModel by viewModels()

    private val mIdeaId = ideaId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostDescriptionBinding.inflate(inflater)
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        viewModel.getIdeaDetails(mIdeaId)
        viewModel.ideaDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    val title = it.body.title.toString()
                    val description = it.body.description.toString()

                    val upvoteCount = it.body.upvoteCount ?: 0
                    val downvoteCount = it.body.downvoteCount ?: 0
                    val upvoteDownvoteCount = upvoteCount - downvoteCount

                    binding.apply {

                        tvTitlePostDescription.text = it.body.title.toString()

                        if (it.body.bookmarked) {
                            ivBookmarkPostDescription.setImageResource(R.drawable.ic_bookmark_check)
                        }
                        tvUpvoteDownvotePostDescription.text = upvoteDownvoteCount.toString()

                        tvBodyPostDescription.text = it.body.body

                        ivSharePostDescription.setOnClickListener {
                            shareIdea(title, description)
                        }

                        ivUpvoteIdeaPostDescription.setOnClickListener {
                            upvoteIdea(mIdeaId)
                        }

                        ivDownvoteIdeaPostDescription.setOnClickListener {
                            downvoteIdea(mIdeaId)
                        }
                    }
                }
            }
        }


        viewModel.upvoteIdea.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                binding.ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_filled_24dp)
                makeToast("Idea Successfully UpVoted!")
            } else {
                makeToast("Unable to UpVote this Idea!")
            }
        }


        viewModel.downvoteIdea.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                binding.ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_filled_24dp)
                makeToast("Idea Successfully DownVoted!")
            } else {
                makeToast("Unable to DownVote this Idea!")
            }
        }
    }


    private fun shareIdea(title: String?, description: String?) {

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, title)
            .putExtra(Intent.EXTRA_TEXT, description)

        requireContext().startActivity(intent)

    }

    private fun upvoteIdea(id: Int) {
        viewModel.upvoteIdea(id.toString())
    }

    private fun downvoteIdea(id: Int) {
        viewModel.downvoteIdea(id.toString())
    }
}