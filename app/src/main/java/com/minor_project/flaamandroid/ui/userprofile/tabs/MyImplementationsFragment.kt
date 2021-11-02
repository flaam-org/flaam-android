package com.minor_project.flaamandroid.ui.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.minor_project.flaamandroid.adapters.MyImplementationsAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.databinding.FragmentMyImplementationsBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.loadSVG
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyImplementationsFragment : Fragment() {
    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyImplementationsBinding

    private val viewModel: MyImplementationsViewModel by viewModels()

    private var myImplementationsList: ArrayList<ImplementationsResponse.Result> = ArrayList()

    private lateinit var myImplementationsAdapter: MyImplementationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyImplementationsBinding.inflate(inflater)

        myImplementationsAdapter =
            MyImplementationsAdapter(this, requireContext(), myImplementationsList)

        binding.rvMyImplementations.setHasFixedSize(true)

        binding.rvMyImplementations.adapter = myImplementationsAdapter

        initObservers()

        return binding.root
    }

    private fun initObservers() {

        myImplementationsAdapter.setToList(arrayListOf())
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch your Profile!")
                }

                is ApiResponse.Success -> {
                    viewModel.getImplementations(it.body.id.toString())
                }
            }
        }


        viewModel.getImplementations.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    if (it.body.results.isNullOrEmpty()) {
                        binding.tvNoUserImplementationsAdded.visibility = View.VISIBLE
                        binding.rvMyImplementations.visibility = View.GONE
                    } else {
                        binding.tvNoUserImplementationsAdded.visibility = View.GONE
                        binding.rvMyImplementations.visibility = View.VISIBLE

                        myImplementationsAdapter.setToList(arrayListOf())

                        myImplementationsAdapter.addToList(it.body.results as ArrayList<ImplementationsResponse.Result>)

                    }

                }
            }
        }


        viewModel.voteImplementation.observe(viewLifecycleOwner) {

            when (it) {
                0 -> {
                }
                -1 -> {
                    makeToast("Implementation Successfully DownVoted!")
                }
                1 -> {
                    makeToast("Implementation Successfully UpVoted!")
                }
            }
            viewModel.getUserProfile()


        }


    }

    fun setOwnerAvatar(ownerAvatar: String, imageView: ImageView) {
        lifecycleScope.launch {
            imageView.loadSVG(ownerAvatar)
        }
    }

    fun voteImplementation(id: Int, vote: Int) {
        viewModel.voteImplementation(id.toString(), vote)
    }

}