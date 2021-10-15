package com.minor_project.flaamandroid.ui.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.adapters.MyBookmarksAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.FragmentMyBookmarksBinding
import com.minor_project.flaamandroid.ui.userprofile.UserProfileFragmentDirections
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyBookmarksFragment : Fragment() {

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyBookmarksBinding

    private val viewModel: MyBookmarksViewModel by viewModels()

    private var myBookmarksList: ArrayList<IdeasResponse.Result> = ArrayList()

    private lateinit var myBookmarksAdapter: MyBookmarksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyBookmarksBinding.inflate(inflater)

        myBookmarksAdapter = MyBookmarksAdapter(this, requireContext(), myBookmarksList)
        binding.rvMyBookmarks.setHasFixedSize(true)

        binding.rvMyBookmarks.adapter = myBookmarksAdapter


        initObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("MyBookmarks : onResume")
        viewModel.getUserProfile()
    }

    private fun initObservers() {
        myBookmarksAdapter.setToList(arrayListOf())
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch your Profile!")
                }

                is ApiResponse.Success -> {
                    viewModel.getIdeas(it.body.id!!)
                }
            }
        }


        viewModel.ideas.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    if (it.body.results.isNullOrEmpty()) {
                        binding.tvNoUserBookmarksAdded.visibility = View.VISIBLE
                        binding.rvMyBookmarks.visibility = View.GONE
                    } else {
                        binding.tvNoUserBookmarksAdded.visibility = View.GONE
                        binding.rvMyBookmarks.visibility = View.VISIBLE

                        myBookmarksAdapter.setToList(arrayListOf())

                        myBookmarksAdapter.addToList(it.body.results as ArrayList<IdeasResponse.Result>)

                        myBookmarksAdapter.setOnClickListener(object :
                            MyBookmarksAdapter.OnClickListener {
                            override fun onClick(ideaId: Int, model: IdeasResponse.Result) {
                                findNavController().navigate(
                                    UserProfileFragmentDirections.actionUserProfileFragmentToPostDetailsFragment(
                                        ideaId
                                    )
                                )
                            }
                        })
                    }

                }
            }
        }

        viewModel.addIdeaToUsersBookmarks.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                viewModel.getUserProfile()
                makeToast("Idea Successfully Added to My Bookmarks!")
            } else {
                makeToast("Unable to Add Idea to My Bookmarks!")
            }
        }


        viewModel.removeIdeaFromUsersBookmarks.observe(viewLifecycleOwner) {

            if (it.isSuccessful) {
                viewModel.getUserProfile()
                makeToast("Idea Successfully Removed to My Bookmarks!")
            } else {
                makeToast("Unable to Remove Idea from My Bookmarks!")
            }
        }
    }

    fun addToBookmark(id: Int) {
        viewModel.addIdeaToUsersBookmarks(id.toString())
    }

    fun removeBookmark(id: Int) {
        viewModel.removeIdeaFromUsersBookmarks(id.toString())
    }

}


