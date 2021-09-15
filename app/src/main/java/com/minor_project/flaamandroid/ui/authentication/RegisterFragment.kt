package com.minor_project.flaamandroid.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.databinding.FragmentRegisterBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)

        initObservers()
        initOnClick()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initOnClick() {
        binding.apply {

            btnRegister.setOnClickListener {
                if (validate()) {
                    viewModel.postRegisterUser(
                        LoginRequest(
                            etEmailRegister.text.toString(),
                            etFnameRegister.text.toString(),
                            etLnameRegister.text.toString(),
                            etPasswordRegister.text.toString(),
                            etUsernameRegister.text.toString()
                        )
                    )
                } else {
                    makeToast("Missing Required Fields!")
                }
            }


        }
    }

    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if (etEmailRegister.text.isNullOrEmpty()) {
                tilEmailFragmentRegister.error = emptyFieldError
                return false
            }
            if (etUsernameRegister.text.isNullOrEmpty()) {
                tilUsernameFragmentRegister.error = emptyFieldError
                return false
            }
            if (etPasswordRegister.text.isNullOrEmpty()) {
                tilPasswordFragmentRegister.error = emptyFieldError
                return false
            }
            if (etFnameRegister.text.isNullOrEmpty()) {
                tilFnameFragmentRegister.error = emptyFieldError
                return false
            }
            if (etLnameRegister.text.isNullOrEmpty()) {
                tilLnameFragmentRegister.error = emptyFieldError
                return false
            }

            return true
        }
    }

    private fun initObservers() {
        viewModel.registerUserResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }
                is ApiResponse.Success -> {
                    findNavController().navigate(R.id.loginFragment)
                    makeToast("User Registered!")
                }
            }
        }
    }


}