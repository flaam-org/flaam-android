package com.minor_project.flaamandroid.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.databinding.FragmentSignUpBinding
import com.minor_project.flaamandroid.utils.ApiException
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)

        initObservers()
        initOnClick()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initOnClick(){
        binding.apply {

            btnSignUp.setOnClickListener {
                if(validate()){
                    viewModel.postRegisterUser(LoginRequest(
                        etPasswordSignIn.text.toString(), etUsername.text.toString(), etEmailSignIn.text.toString()
                    ))
                }
                else{
                    makeToast("missing required fields!")
                }
            }


        }
    }

    private fun validate(): Boolean{
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if(etEmailSignIn.text.isNullOrEmpty()){
                tilEmailFragmentSignIn.error = emptyFieldError
                return false
            }
            if(etUsername.text.isNullOrEmpty()){
                tilNameFragmentSignIn.error = emptyFieldError
                return false
            }
            if(etPasswordSignIn.text.isNullOrEmpty()){
                tilPassFragmentSignIn.error = emptyFieldError
                return false
            }
            return true
        }
    }

    private fun initObservers(){
        viewModel.registerUserResult.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> {
                    makeToast(it.message.toString())
                }
                is ApiException.Success -> {
                    findNavController().navigate(R.id.loginFragment)
                    makeToast("User Registered!")
                }
            }
        }
    }



}