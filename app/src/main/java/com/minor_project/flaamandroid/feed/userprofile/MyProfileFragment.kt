package com.minor_project.flaamandroid.feed.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.UpdateProfileRequest
import com.minor_project.flaamandroid.databinding.FragmentMyProfileBinding
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private val viewModel: MyProfileViewModel by viewModels()
    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentMyProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater)
        binding.apply {
            btnUpdateMyProfile.setOnClickListener {
                updateUserProfile()
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserProfile()

        viewModel.userProfile.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiException.Success -> {
                    runBlocking {
                        binding.apply {
                            etUsernameMyProfile.setText(it.body.username.toString())
                            etFnameMyProfile.setText(it.body.firstName.toString())
                            etLnameMyProfile.setText(it.body.lastName.toString())
                            etEmailMyProfile.setText(it.body.email.toString())
                            tvMyProfileDoj.text = (it.body.dateJoined.toString())
                        }
                    }

                }
            }
        }


        viewModel.updateUserProfile.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> {
                    makeToast("Unable to Update Data")
                }

                is ApiException.Success -> {
                    makeToast("Updated Profile Successfully")
                }
            }
        }

    }

    private fun updateUserProfile() {
        viewModel.updateUserProfile(UpdateProfileRequest(binding.etFnameMyProfile.text.toString(), binding.etLnameMyProfile.text.toString()))
    }


}