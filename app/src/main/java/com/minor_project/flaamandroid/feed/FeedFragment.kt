package com.minor_project.flaamandroid.feed

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
import com.minor_project.flaamandroid.adapters.FeedPostAdapter
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding
import com.minor_project.flaamandroid.models.FeedPostModel
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding : FragmentFeedBinding
    private var initialId = R.id.step1
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFeedBinding.inflate(inflater)

        initObservers()

        binding.apply {

            binding.efabPostIdea.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostIdeaFragment())
            }

            binding.civFeedFragmentMyProfile.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToUserProfileFragment())
            }



        }


        binding.motionLayout.addTransitionListener(object: MotionLayout.TransitionListener{
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

                if(initialId == R.id.step1 && currentId == R.id.step2){
                    motionLayout?.transitionToState(R.id.step3, 500)
                }

                if(initialId == R.id.step2 && currentId == R.id.step3){

                    val animation = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, 380F)

                    animation.duration = 200
                    animation.addListener(object: Animator.AnimatorListener{
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {

                            motionLayout?.transitionToState(R.id.step4, 200)
                            animation?.removeAllListeners()
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })

                    animation.start()

                }

                if(currentId == R.id.step4){

                    Timber.e("click init")
                    binding.imageView.setOnClickListener {
                        motionLayout?.transitionToState(R.id.step3,500)
                    }
                }

                if(initialId == R.id.step3 && currentId == R.id.step2){
                    motionLayout?.transitionToState(R.id.step1, 200)
                }

                if(initialId == R.id.step4 && currentId == R.id.step3){

                    val animation = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, 0F)

                    animation.duration = 200
                    animation.addListener(object: Animator.AnimatorListener{
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            motionLayout?.transitionToState(R.id.step2, 200)
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


    private fun initObservers(){
        var timer = Timer()
        val DELAY = 800L
        binding.include.etTagName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()

                timer = Timer()
                timer.schedule(object: TimerTask(){
                    override fun run() {
                        viewModel.getTagsForKeyword(s.toString())
                    }

                }, DELAY)


            }

        })


        viewModel.tags.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> makeToast(it.message.toString())
                is ApiException.Success -> showTagsMenuPopup(it.body)
            }
        }

    }

    private fun showTagsMenuPopup(data: TagsResponse){

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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedPostList = ArrayList<FeedPostModel>()

        val n = FeedPostModel(
            R.drawable.ic_profile_image_place_holder,"Idea/Post Title", 2, 4, resources.getString(
                R.string.sample_text
            ))

        feedPostList.add(n)
        binding.rvFeedPosts.layoutManager = LinearLayoutManager(context)

        binding.rvFeedPosts.setHasFixedSize(true)


        val feedPostAdapter = FeedPostAdapter(requireContext(), feedPostList)
        binding.rvFeedPosts.adapter = feedPostAdapter
    }
}