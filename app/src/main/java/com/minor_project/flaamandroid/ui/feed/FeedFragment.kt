package com.minor_project.flaamandroid.ui.feed

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.IdeaResponseItem
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var ideasList: ArrayList<IdeaResponseItem> = ArrayList()

    private lateinit var binding: FragmentFeedBinding
    private var initialId = R.id.step1
    private val viewModel: FeedViewModel by viewModels()
    private var isFilterLayoutVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFeedBinding.inflate(inflater)


        binding.rvFeedPosts.layoutManager = LinearLayoutManager(context)

        binding.rvFeedPosts.setHasFixedSize(true)

        initObservers()

        binding.apply {

            binding.llCardview.setOnClickListener {
                if (isFilterLayoutVisible) {
                    Timber.e(binding.motionLayout.currentState.toString())
                    if (binding.motionLayout.currentState == R.id.step4) {
                        motionLayout.transitionToState(R.id.step3, 500)
                    }

                }
            }

            binding.efabPostIdea.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostIdeaFragment())
            }

            binding.civFeedFragmentMyProfile.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToUserProfileFragment())
            }


        }


        binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

                initialId = startId

                Timber.e(startId.toString() + "transtart")
                Timber.e(endId.toString() + "transend")

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {


            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                if (initialId == R.id.step1 && currentId == R.id.step2) {
                    motionLayout?.transitionToState(R.id.step3, 500)

                }

                if (initialId == R.id.step2 && currentId == R.id.step3) {

                    val animation =
                        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, 380F)

                    animation.duration = 200
                    animation.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {

                            motionLayout?.transitionToState(R.id.step4, 200)
                            isFilterLayoutVisible = true
                            binding.efabPostIdea.isEnabled = false
                            animation?.removeAllListeners()
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })

                    animation.start()

                }

                if (currentId == R.id.step4) {

                    Timber.e("click init")
                    binding.imageView.setOnClickListener {
                        motionLayout?.transitionToState(R.id.step3, 500)

                    }
                }

                if (initialId == R.id.step3 && currentId == R.id.step2) {
                    motionLayout?.transitionToState(R.id.step1, 200)
                    isFilterLayoutVisible = false
                }

                if (initialId == R.id.step4 && currentId == R.id.step3) {

                    val animation =
                        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, 0F)

                    animation.duration = 200
                    animation.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            motionLayout?.transitionToState(R.id.step2, 200)
                            binding.efabPostIdea.isEnabled = true
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })

                    animation.start()


                }


            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }


        })

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun initObservers() {

        viewModel.getIdeas()
        viewModel.getIdeas.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    ideasList = (it.body.results as ArrayList<IdeaResponseItem>?)!!
                    val feedPostAdapter = FeedPostAdapter(this, requireContext(), ideasList)
                    binding.rvFeedPosts.adapter = feedPostAdapter


                    feedPostAdapter.setOnClickListener(object : FeedPostAdapter.OnClickListener {
                        override fun onClick(position: Int, model: IdeaResponseItem) {
                            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostDetailsFragment())
                        }

                    })
                }
            }
        }


        var timer = Timer()
        val DELAY = 800L
        binding.include.etTagName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.getTagsForKeyword(s.toString())
                    }
                }, DELAY)
            }

        })


        viewModel.tags.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> showTagsMenuPopup(it.body)
            }
        }

    }

    private fun showTagsMenuPopup(data: TagsResponse) {

        val menuPopup = PopupMenu(requireContext(), binding.include.etTagName)

        data.tagsResponseItems?.forEach {
            menuPopup.menu.add(it.name)
        }

        menuPopup.setOnMenuItemClickListener {
            val chip = Chip(requireContext())
            chip.text = it.title

            binding.include.cgTags.addView(chip)

            return@setOnMenuItemClickListener true
        }

        menuPopup.show()
    }

    fun getTagsListFromIds(tagsIdList: List<Int>?): List<String> {
        val tagsListName: ArrayList<String> = ArrayList()
        viewModel.getTagsFromIds(tagsIdList)
        viewModel.tagsListFromIds.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> {
                    for (tag in it.body.tagsResponseItems!!) {
                        tagsListName.add(tag.name!!)
                    }
                }
            }
        }

        return tagsListName
    }

}