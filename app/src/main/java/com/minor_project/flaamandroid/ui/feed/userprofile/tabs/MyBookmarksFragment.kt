package com.minor_project.flaamandroid.ui.feed.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.FragmentMyBookmarksBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.getDaysDiff
import com.minor_project.flaamandroid.utils.loadImage
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBookmarksFragment : Fragment() {

    @Inject
    lateinit var preferences: UserPreferences

    private var ideasList: ArrayList<IdeasResponse.Result> = ArrayList()
    private var bookmarkedIdeasListIds: ArrayList<Int> = ArrayList()

    private lateinit var binding: FragmentMyBookmarksBinding
//    private lateinit var myBookmarksAdapter: MyBookmarksAdapter
    private val viewModel: MyBookmarksViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyBookmarksBinding.inflate(inflater)

//        myBookmarksAdapter = MyBookmarksAdapter(this, requireContext(), ideasList)
//        myBookmarksAdapter.setOnClickListener(object : MyBookmarksAdapter.OnClickListener {
//            override fun onClick(position: Int, model: IdeasResponse.Result) {
//                findNavController().navigate(MyBookmarksFragmentDirections.actionMyBookmarksFragmentToPostDetailsFragment())
//            }
//
//        })
//        binding.rvMyBookmarks.setHasFixedSize(true)
//
//        binding.rvMyBookmarks.adapter = myBookmarksAdapter

//        initObservers()

        return binding.root
    }

//    private fun initObservers() {
//
//        viewModel.getUserProfile()
//        viewModel.userProfile.observe(viewLifecycleOwner) {
//            when (it) {
//                is ApiResponse.Error -> {
//                    makeToast("Unable to fetch BookMarks!")
//                }
//
//                is ApiResponse.Success -> {
//                    bookmarkedIdeasListIds.addAll(it.body.bookmarkedIdeas!!)
//                }
//            }
//        }
//
//
//        viewModel.updateUserProfile.observe(viewLifecycleOwner) {
//            when (it) {
//                is ApiResponse.Error -> {
//                    makeToast("Unable to Update Data")
//                }
//
//                is ApiResponse.Success -> {
//                    makeToast("Updated Profile Successfully")
//                }
//            }
//        }
//    }
//
//    fun updateUserProfile(bookmarkedIdeas: List<Int>?) {
//        viewModel.updateUserProfile(
//            UpdateProfileRequest(
//                null,
//                bookmarkedIdeas,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//            )
//        )
//    }


}