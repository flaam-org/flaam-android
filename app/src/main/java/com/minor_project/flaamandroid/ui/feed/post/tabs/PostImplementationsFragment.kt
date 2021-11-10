package com.minor_project.flaamandroid.ui.feed.post.tabs

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.minor_project.flaamandroid.adapters.PostImplementationsAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.databinding.FragmentPostImplementationsBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.loadSVG
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.progressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostImplementationsFragment(ideaId: Int) : Fragment() {

    private lateinit var binding: FragmentPostImplementationsBinding

    private lateinit var postImplementationsAdapter: PostImplementationsAdapter

    private var postImplementationsList: ArrayList<ImplementationsResponse.Result> = ArrayList()

    private val viewModel: PostImplementationsViewModel by viewModels()

    private val mIdeaId = ideaId

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var mProgressDialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostImplementationsBinding.inflate(inflater)

        postImplementationsAdapter =
            PostImplementationsAdapter(this, requireContext(), postImplementationsList)

        binding.rvImplementations.setHasFixedSize(true)

        binding.rvImplementations.adapter = postImplementationsAdapter

        mProgressDialog = requireContext().progressDialog()

        initObservers()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mProgressDialog.dismiss()
    }

    private fun initObservers() {

        postImplementationsAdapter.setToList(arrayListOf())
        mProgressDialog.show()
        viewModel.getImplementations(mIdeaId.toString())
        viewModel.getImplementations.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    mProgressDialog.dismiss()
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    mProgressDialog.dismiss()
                    if (it.body.results.isNullOrEmpty()) {
                        binding.tvNoImplementationsAdded.visibility = View.VISIBLE
                        binding.rvImplementations.visibility = View.GONE
                    } else {
                        binding.tvNoImplementationsAdded.visibility = View.GONE
                        binding.rvImplementations.visibility = View.VISIBLE

                        postImplementationsAdapter.setToList(arrayListOf())

                        postImplementationsAdapter.addToList(it.body.results as ArrayList<ImplementationsResponse.Result>)

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
            viewModel.getImplementations(mIdeaId.toString())


        }


    }

    fun setOwnerAvatar(ownerAvatar: String, imageView: ImageView) {
        lifecycleScope.launch(Dispatchers.IO) {
            imageView.loadSVG(ownerAvatar)
        }
    }

    fun voteImplementation(id: Int, vote: Int) {
        viewModel.voteImplementation(id.toString(), vote)
    }

    fun openRepository(repoUrl: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(repoUrl)
        startActivity(openURL)
    }

}