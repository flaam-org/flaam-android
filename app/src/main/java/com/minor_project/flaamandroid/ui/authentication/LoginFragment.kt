package com.minor_project.flaamandroid.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.data.response.LoginResponse
import com.minor_project.flaamandroid.databinding.FragmentLoginBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var preferences: UserPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        initObservers()
        initOnClick()
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun initOnClick(){
        binding.apply {
            btnLogin.setOnClickListener {
                if(validate()){
                    viewModel.postLoginRequest(LoginRequest(null, null, null, etPasswordLogin.text.toString(), etUsernameLogin.text.toString(), null))
                }else{
                    makeToast("missing fields!")
                }
            }
        }

    }

    private fun initObservers(){
        viewModel.loginResult.observe(viewLifecycleOwner){
            when(it){
                is ApiResponse.Error -> makeToast(it.message.toString())
                is ApiResponse.Success -> {
                    runBlocking {
                        runBlocking {
                            preferences.updateTokens(it.body)
                        }
                        makeToast("user logged in! ${preferences.accessToken.first()}")
                        viewModel.getUserProfile()
                    }

                }
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner){
            when(it){
                is ApiResponse.Error -> {
                    runBlocking { preferences.updateTokens(LoginResponse(null, null)) }
                }

                is ApiResponse.Success -> {
                    runBlocking {
                        preferences.registerUser(it.body)
                    }

                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedFragment())
                }
            }
        }
    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Is Required!"
        binding.apply {
            if(etUsernameLogin.text.isNullOrEmpty()){
                tilUsernameFragmentLogin.error = emptyFieldError
                return false
            }
            if(etPasswordLogin.text.isNullOrEmpty()){
                tilPasswordFragmentLogin.error = emptyFieldError
                return false
            }
            return true
        }


    }


}