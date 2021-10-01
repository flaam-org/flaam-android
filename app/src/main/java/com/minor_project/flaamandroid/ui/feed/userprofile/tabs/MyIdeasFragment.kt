package com.minor_project.flaamandroid.ui.feed.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.FragmentMyIdeasBinding
import com.minor_project.flaamandroid.ui.feed.FeedFragmentDirections
import com.minor_project.flaamandroid.ui.feed.FeedPostAdapter
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyIdeasFragment : Fragment() {

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyIdeasBinding

    private val viewModel: MyIdeasViewModel by viewModels()

    private var myIdeasList: ArrayList<IdeasResponse.Result> = ArrayList()

    private lateinit var myIdeasAdapter: MyIdeasAdapter

    private var bookmarkedIdeas: ArrayList<Int> = ArrayList()

    private var ownerId: Int = 55

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyIdeasBinding.inflate(inflater)

        myIdeasAdapter = MyIdeasAdapter(this, requireContext(), myIdeasList)
        binding.rvMyIdeas.setHasFixedSize(true)

        binding.rvMyIdeas.adapter = myIdeasAdapter

        initObservers()
        return binding.root
    }

    private fun initObservers() {

        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch BookMarks!")
                }

                is ApiResponse.Success -> {
                    ownerId = it.body.id!!
                    bookmarkedIdeas.addAll(it.body.bookmarkedIdeas!!)
                }
            }
        }

        viewModel.getIdeas(ownerId!!)
        viewModel.ideas.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {

                    myIdeasAdapter.addToList(it.body.results as ArrayList<IdeasResponse.Result>)

                    myIdeasAdapter.setOnClickListener(object : MyIdeasAdapter.OnClickListener {
                        override fun onClick(position: Int, model: IdeasResponse.Result) {
                            findNavController().navigate(MyIdeasFragmentDirections.actionMyIdeasFragmentToPostDetailsFragment())
                        }
                    })


                }
            }
        }

        viewModel.addIdeaToUsersBookmarks.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unabe to Add Idea to My Bookmarks!")
                }

                is ApiResponse.Success -> {
                    makeToast("Idea Successfully Added to My Bookmarks!")
                }
            }
        }


        viewModel.removeIdeaFromUsersBookmarks.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to Remove Idea from My Bookmarks!")
                }

                is ApiResponse.Success -> {
                    makeToast("Idea Successfully Removed to My Bookmarks!")
                }
            }
        }
    }

    fun addToBookmark(id: Int) {
        viewModel.addIdeaToUsersBookmarks(id.toString())
    }

    fun removeBookmark(id: Int) {
        viewModel.removeIdeaFromUsersBookmarks(id.toString())
    }

    fun checkUserBookmarks(id: Int): Boolean {
        viewModel.getUserProfile()
        return bookmarkedIdeas.contains(id)
    }
}
