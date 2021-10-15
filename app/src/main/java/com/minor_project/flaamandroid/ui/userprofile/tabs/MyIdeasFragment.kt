package com.minor_project.flaamandroid.ui.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.adapters.MyIdeasAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.DeleteIdeaRequest
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.FragmentMyIdeasBinding
import com.minor_project.flaamandroid.ui.userprofile.UserProfileFragmentDirections
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.hideProgressDialog
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyIdeasFragment : Fragment() {

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyIdeasBinding

    private val viewModel: MyIdeasViewModel by viewModels()

    private var myIdeasList: ArrayList<IdeasResponse.Result> = ArrayList()

    private lateinit var myIdeasAdapter: MyIdeasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyIdeasBinding.inflate(inflater)

        myIdeasAdapter = MyIdeasAdapter(this, requireContext(), myIdeasList)
        binding.rvMyIdeas.setHasFixedSize(true)

        binding.rvMyIdeas.adapter = myIdeasAdapter

        initObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("MyIdeas : onResume")
        viewModel.getUserProfile()
    }

    private fun initObservers() {
        myIdeasAdapter.setToList(arrayListOf())
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
                        binding.tvNoUserIdeasAdded.visibility = View.VISIBLE
                        binding.rvMyIdeas.visibility = View.GONE
                    } else {
                        binding.tvNoUserIdeasAdded.visibility = View.GONE
                        binding.rvMyIdeas.visibility = View.VISIBLE

                        myIdeasAdapter.setToList(arrayListOf())

                        myIdeasAdapter.addToList(it.body.results as ArrayList<IdeasResponse.Result>)

                        myIdeasAdapter.setOnClickListener(object : MyIdeasAdapter.OnClickListener {
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

//        viewModel.deleteIdea.observe(viewLifecycleOwner) { res ->
//            when (res) {
//                is ApiResponse.Error -> {
//                    makeToast(res.message.toString())
//                }
//                is ApiResponse.Success -> {
//                    viewModel.getUserProfile()
//                    makeToast("Idea Deleted Successfully!")
//                }
//            }
//        }
    }

    fun addToBookmark(id: Int) {
        viewModel.addIdeaToUsersBookmarks(id.toString())
    }

    fun removeBookmark(id: Int) {
        viewModel.removeIdeaFromUsersBookmarks(id.toString())
    }

    fun deleteIdea(id: Int, model: IdeasResponse.Result) {
        val tagsIdList: ArrayList<Int> = ArrayList()
        model.tags!!.forEach {
            tagsIdList.add(it.id!!)
        }
        Timber.i("IdeaTagsIdList" + tagsIdList)

        makeToast("This Idea will be Deleted Soon!")

//        viewModel.deleteIdea(
//            id, DeleteIdeaRequest(
//                model.body,
//                model.description,
//                model.draft,
//                model.milestones as List<List<String>>,
//                tagsIdList,
//                model.title
//            )
//        )

    }

}
