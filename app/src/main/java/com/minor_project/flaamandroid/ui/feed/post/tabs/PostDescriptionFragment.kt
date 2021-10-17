package com.minor_project.flaamandroid.ui.feed.post.tabs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentPostDescriptionBinding
import com.minor_project.flaamandroid.ui.feed.post.PostDetailsFragmentDirections
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.hideProgressDialog
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        requireContext().showProgressDialog()
        viewModel.getIdeaDetails(mIdeaId)
        viewModel.ideaDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    requireContext().hideProgressDialog()
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    requireContext().hideProgressDialog()
                    val title = it.body.title.toString()
                    val description = it.body.description.toString()

                    val upvoteCount = it.body.upvoteCount ?: 0
                    val downvoteCount = it.body.downvoteCount ?: 0
                    val upvoteDownvoteCount = upvoteCount - downvoteCount

                    val vote = it.body.vote

                    val ownerAvatar = it.body.ownerAvatar

                    binding.apply {
                        when (vote) {
                            1 -> {
                                ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_filled_24dp)
                                disableUpvoteDownvote()
                            }
                            -1 -> {
                                ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_filled_24dp)
                                disableUpvoteDownvote()
                            }
                            else -> {
                                ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_outline_24dp)
                                ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_outline_24dp)

                                ivUpvoteIdeaPostDescription.setOnClickListener {
                                    upvoteIdea(mIdeaId)
                                    disableUpvoteDownvote()
                                }

                                ivDownvoteIdeaPostDescription.setOnClickListener {
                                    downvoteIdea(mIdeaId)
                                    disableUpvoteDownvote()
                                }
                            }
                        }


                        val imageLoader = ImageLoader.Builder(requireContext())
                            .componentRegistry {
                                add(SvgDecoder(requireContext()))
                            }
                            .build()

                        val request = ImageRequest.Builder(requireContext())
                            .data(ownerAvatar.toString())
                            .build()
                        lifecycleScope.launch(Dispatchers.Main) {
                            val drawable = imageLoader.execute(request).drawable
                            civUserImagePostDescription.setImageDrawable(drawable)
                        }



                        for (tag in it.body.tags!!) {
                            val chip = Chip(requireContext())
                            chip.text = tag.name.toString()
                            binding.cgTagsPostDescription.addView(chip)
                        }




                        tvTitlePostDescription.text = it.body.title.toString()

                        if (it.body.bookmarked) {
                            ivBookmarkPostDescription.setImageResource(R.drawable.ic_bookmark_check)
                        }
                        tvUpvoteDownvotePostDescription.text = upvoteDownvoteCount.toString()

                        tvBodyPostDescription.text = it.body.body

                        ivSharePostDescription.setOnClickListener {
                            shareIdea(title, description)
                        }

                        ivAddImplementationPostDescription.setOnClickListener {
                            findNavController().navigate(
                                PostDetailsFragmentDirections.actionPostDetailsFragmentToAddImplementationFragment(
                                    mIdeaId
                                )
                            )
                        }
                    }
                }
            }
        }


        viewModel.upvoteIdea.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                binding.ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_filled_24dp)
                viewModel.getIdeaDetails(mIdeaId)
                makeToast("Idea Successfully UpVoted!")
            } else {
                makeToast("Unable to UpVote this Idea!")
            }
        }


        viewModel.downvoteIdea.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                binding.ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_filled_24dp)
                viewModel.getIdeaDetails(mIdeaId)
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

    private fun disableUpvoteDownvote() {
        binding.apply {
            ivUpvoteIdeaPostDescription.isClickable = false
            ivDownvoteIdeaPostDescription.isClickable = false
        }
    }
}


