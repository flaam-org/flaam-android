package com.minor_project.flaamandroid.ui.userprofile.tabs

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
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.adapters.MyImplementationsAdapter
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.databinding.FragmentMyImplementationsBinding
import com.minor_project.flaamandroid.ui.userprofile.UserProfileFragmentDirections
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyImplementationsFragment : Fragment() {
    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyImplementationsBinding

    private val viewModel: MyImplementationsViewModel by viewModels()

    private var myImplementationsList: ArrayList<ImplementationsResponse.Result> = ArrayList()

    private lateinit var myImplementationsAdapter: MyImplementationsAdapter

    private lateinit var mProgressDialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyImplementationsBinding.inflate(inflater)

        myImplementationsAdapter =
            MyImplementationsAdapter(this, requireContext(), myImplementationsList)

        binding.rvMyImplementations.setHasFixedSize(true)

        binding.rvMyImplementations.adapter = myImplementationsAdapter

        mProgressDialog = requireContext().progressDialog()

        initObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("MyImplementations : onResume")
        mProgressDialog.show()
        viewModel.getUserProfile()
    }

    override fun onPause() {
        super.onPause()
        mProgressDialog.dismiss()
    }

    private fun initObservers() {
        myImplementationsAdapter.setToList(arrayListOf())
        mProgressDialog.show()
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    mProgressDialog.dismiss()
                    makeToast("Unable to fetch your Profile!")
                }

                is ApiResponse.Success -> {
                    mProgressDialog.dismiss()
                    mProgressDialog.show()
                    viewModel.getImplementations(it.body.id.toString())
                }
            }
        }


        viewModel.getImplementations.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    mProgressDialog.dismiss()
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    mProgressDialog.dismiss()
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

        viewModel.deleteImplementation.observe(viewLifecycleOwner) {

            when (it) {
                -1 -> {
                    makeToast("Implementation could not be Deleted!")
                }
                1 -> {
                    makeToast("Implementation Successfully Deleted!")
                }
            }
            viewModel.getUserProfile()
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

    fun deleteMyImplementation(implementationId: Int) {
        viewModel.deleteImplementation(implementationId)
    }

    fun editImplementation(implementationId: Int) {
        findNavController().navigate(
            UserProfileFragmentDirections.actionUserProfileFragmentToEditImplementationFragment(
                implementationId
            )
        )
    }

}