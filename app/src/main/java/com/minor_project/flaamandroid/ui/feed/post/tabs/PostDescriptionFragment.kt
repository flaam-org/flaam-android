package com.minor_project.flaamandroid.ui.feed.post.tabs

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class PostDescriptionFragment(ideaId: Int) : Fragment() {

    private lateinit var binding: FragmentPostDescriptionBinding

    private val viewModel: PostDescriptionViewModel by viewModels()

    private val mIdeaId = ideaId
    private var vote by Delegates.notNull<Int>()

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

                    vote = it.body.vote ?: 0

                    val ownerAvatar = it.body.ownerAvatar

                    val tagsList = it.body.tags!!


                    binding.apply {
                        ivUpvoteIdeaPostDescription.setOnClickListener {
                            if (vote == 1) {
                                voteIdea(0)
                            } else {
                                voteIdea(1)
                            }

                        }

                        ivDownvoteIdeaPostDescription.setOnClickListener {
                            if (vote == -1) {
                                voteIdea(0)
                            } else {
                                voteIdea(-1)
                            }
                        }
                    }


                    binding.apply {
                        when (vote) {
                            1 -> {
                                ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_filled_24dp)
                            }
                            -1 -> {
                                ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_filled_24dp)
                            }
                            else -> {
                                ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_outline_24dp)
                                ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_outline_24dp)
                            }
                        }


                        lifecycleScope.launch(Dispatchers.Main) {
                            civUserImagePostDescription.loadSVG(ownerAvatar.toString())
                        }

                        Timber.e(tagsList.toString())

                        cgTagsPostDescription.removeAllViews()

                        tagsList.indices.forEach { i ->
                            val chip = Chip(context)

                            Timber.e(tagsList[i].toString())
                            if (i < 4) {
                                chip.text = tagsList[i].name
                                chip.chipBackgroundColor =
                                    ColorStateList.valueOf(listOfChipColors[i])
                                chip.setTextColor(Color.WHITE)
                                cgTagsPostDescription.addView(chip)
                            }
                            if (i == 4) {
                                val remainingTagsCount = tagsList.size - 4
                                chip.text = resources.getString(
                                    R.string.plus_number_of_tags,
                                    remainingTagsCount.toString()
                                )
                                chip.setOnClickListener { t ->
                                    t.showRemainingTagsPopup(tagsList.subList(4, tagsList.size))
                                }
                                cgTagsPostDescription.addView(chip)
                            }

                        }




                        tvTitlePostDescription.text = it.body.title.toString()

                        if (it.body.bookmarked == true) {
                            ivBookmarkPostDescription.setImageResource(R.drawable.ic_bookmark_check)
                        }
                        tvUpvoteDownvotePostDescription.text = upvoteDownvoteCount.toString()


                        Timber.e(it.body.description.toString())
                        Timber.e(it.body.body)
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


        viewModel.voteIdea.observe(viewLifecycleOwner) {

            binding.tvUpvoteDownvotePostDescription.text =
                (binding.tvUpvoteDownvotePostDescription.text.toString()
                    .toInt() + it - vote).toString()


            vote = it

            binding.apply {

                ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_outline_24dp)
                ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_outline_24dp)

                when (it) {
                    1 -> {
                        ivUpvoteIdeaPostDescription.setImageResource(R.drawable.ic_upvote_filled_24dp)
                        makeToast("Idea Successfully UpVoted!")
                    }
                    -1 -> {
                        ivDownvoteIdeaPostDescription.setImageResource(R.drawable.ic_downvote_filled_24dp)
                        makeToast("Idea Successfully DownVoted!")
                    }
                }

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

    private fun voteIdea(value: Int) {
        viewModel.voteIdea(mIdeaId, value)
    }

}


