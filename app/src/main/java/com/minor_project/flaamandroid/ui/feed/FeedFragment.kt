package com.minor_project.flaamandroid.ui.feed

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.adapters.FeedPostAdapter
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.FragmentFeedBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var ideasList: ArrayList<IdeasResponse.Result> = ArrayList()

    private var areExtraViewsVisible = true

    private lateinit var binding: FragmentFeedBinding
    private var initialId = R.id.step1
    private val viewModel: FeedViewModel by viewModels()
    private var isFilterLayoutVisible: Boolean = false
    private var isRequestDispatched = false


    private var orderBy: String? = null
    private var tags = arrayListOf<Int>()
    private var searchQuery: String? = null

    private lateinit var feedPostAdapter: FeedPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFeedBinding.inflate(inflater)

        feedPostAdapter = FeedPostAdapter(this, requireContext(), ideasList)
        binding.rvFeedPosts.setHasFixedSize(true)

        binding.rvFeedPosts.adapter = feedPostAdapter



        initObservers()

        binding.apply {


            llFeedSearchBar.setOnClickListener {
                collapseFilterView()
            }

            tilSearch.editText?.setOnEditorActionListener { _, actionId, _ ->

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    searchQuery = tilSearch.editText!!.text.toString()
                    refreshFeed()
                }

                return@setOnEditorActionListener true

            }

            efabPostIdea.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToPostIdeaFragment())
            }

            civFeedFragmentMyProfile.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToUserProfileFragment())
            }


            rvFeedPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    // load more ideas if user reaches end! and when there are no pending requests!
                    if (!recyclerView.canScrollVertically(Constants.DOWN) && !feedPostAdapter.isEndReached && !isRequestDispatched) {
                        isRequestDispatched = true
                        viewModel.getIdeas(feedPostAdapter.list.size, orderBy, tags, searchQuery)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)


                    if (dy > 0 && areExtraViewsVisible) {
                        areExtraViewsVisible = !areExtraViewsVisible
                        toggleViewsVisibility(false)
                    }

                    if (dy < 0 && !areExtraViewsVisible) {
                        areExtraViewsVisible = !areExtraViewsVisible
                        toggleViewsVisibility(true)
                    }


                }
            })

            include.cgFilter.isSingleSelection = true
            include.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                val includedView = binding.include

                when (checkedId) {
                    R.id.chipAll -> {
                        orderBy = null
                    }
                    R.id.chipImpl -> {
                        orderBy = "implementation_count"
                    }
                    R.id.chipLatest -> {
                        orderBy = "created_at"
                    }
                    R.id.chipUpVotes -> {
                        orderBy = "upvote_count"
                    }
                    R.id.chipVotes -> {
                        orderBy = "vote"
                    }
                    -1 -> {
                        includedView.chipAll.isChecked = true
                    }
                }
                refreshFeed()


            }


        }


        binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

                initialId = startId

                Timber.e(startId.toString() + "tranStart")
                Timber.e(endId.toString() + "tranSend")

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

    private fun collapseFilterView() {
        if (isFilterLayoutVisible) {
            Timber.e(binding.motionLayout.currentState.toString())
            if (binding.motionLayout.currentState == R.id.step4) {
                binding.motionLayout.transitionToState(R.id.step3, 500)
            }
        }
    }

    private fun refreshFeed() {
        feedPostAdapter.list.clear()
        feedPostAdapter.isEndReached = false
        feedPostAdapter.notifyDataSetChanged()
        isRequestDispatched = true
        viewModel.getIdeas(0, orderBy, tags, searchQuery)
    }

    private fun toggleViewsVisibility(isVisible: Boolean) {

        if (isVisible) {
            binding.apply {
                isVisible.also {
                    llFeedSearchBar.changeVisibilityWithAnimation(it)
                    cardViewFilter.changeVisibilityWithAnimation(it)
                    efabPostIdea.changeVisibilityWithAnimation(it)
                }
            }
        } else {
            binding.apply {
                isVisible.also {
                    llFeedSearchBar.changeVisibilityWithAnimation(it)
                    cardViewFilter.changeVisibilityWithAnimation(it)
                    efabPostIdea.changeVisibilityWithAnimation(it)

                }
            }
        }

    }

    private fun View.changeVisibilityWithAnimation(setVisible: Boolean) {
        val v = this

        if (setVisible) {

            val va = ValueAnimator.ofFloat(0f, 1f)
            va.duration = 300
            va.addUpdateListener { animation ->
                this.alpha = animation.animatedValue as Float
            }

            va.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {

                    true.also {
                        v.isClickable = it
                        v.isFocusable = it
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }

            })
            va.start()

        } else {
            val va = ValueAnimator.ofFloat(1f, 0f)
            va.duration = 300
            va.addUpdateListener { animation ->
                this.alpha = animation.animatedValue as Float
            }

            va.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    false.also {
                        v.isClickable = it
                        v.isFocusable = it
                    }

                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }

            })
            va.start()

        }


    }

    private fun initObservers() {
        isRequestDispatched = true

        feedPostAdapter.setToList(arrayListOf())

        viewModel.resetIdeas()
        refreshFeed()
        viewModel.ideas.observe(viewLifecycleOwner) {
            isRequestDispatched = false
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    if (it.body.results?.size!! < 5) {
                        feedPostAdapter.isEndReached = true
                    }

                    feedPostAdapter.addToList(it.body.results as ArrayList<IdeasResponse.Result>)



                    feedPostAdapter.setOnClickListener(object : FeedPostAdapter.OnClickListener {
                        override fun onClick(ideaId: Int, model: IdeasResponse.Result) {
                            findNavController().navigate(
                                FeedFragmentDirections.actionFeedFragmentToPostDetailsFragment(
                                    ideaId
                                )
                            )
                        }

                    })


                }
            }
        }


        viewModel.addIdeaToUsersBookmarks.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                refreshFeed()
                makeToast("Idea Successfully Added to My Bookmarks!")
            } else {
                makeToast("Unable to Add Idea to My Bookmarks!")
            }
        }


        viewModel.removeIdeaFromUsersBookmarks.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                refreshFeed()
                makeToast("Idea Successfully Removed to My Bookmarks!")
            } else {
                makeToast("Unable to Remove Idea from My Bookmarks!")
            }
        }


        var timer = Timer()
        val delay = 800L
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
                }, delay)
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

        data.results?.forEach {
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


    fun addToBookmark(id: Int) {
        viewModel.addIdeaToUsersBookmarks(id.toString())
    }

    fun removeBookmark(id: Int) {
        viewModel.removeIdeaFromUsersBookmarks(id.toString())
    }

    fun setOwnerAvatar(ownerAvatar: String, imageView: ImageView) {
        lifecycleScope.launch {
            imageView.loadSVG(ownerAvatar)
        }
    }
}