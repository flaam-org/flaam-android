package com.minor_project.flaamandroid.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.LoginRequest
import com.minor_project.flaamandroid.databinding.FragmentLoginBinding
import com.minor_project.flaamandroid.utils.ApiException
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
            btnSignIn.setOnClickListener {
                if(validate()){
                    viewModel.postLoginRequest(LoginRequest(etPasswordSignIn.text.toString(), etEmailSignIn.text.toString(), null))
                }else{
                    makeToast("missing fields!")
                }
            }
        }

    }

    private fun initObservers(){
        viewModel.loginResult.observe(viewLifecycleOwner){
            when(it){
                is ApiException.Error -> makeToast(it.message.toString())
                is ApiException.Success -> {
                    runBlocking {
                        preferences.updateTokens(it.body)
                        makeToast("user logged in! ${preferences.getToken().first()}")
                    }

                }
            }
        }
    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Is Required!"
        binding.apply {
            if(etEmailSignIn.text.isNullOrEmpty()){
                tilUsernameLogin.error = emptyFieldError
                return false
            }
            if(etPasswordSignIn.text.isNullOrEmpty()){
                tilPasswordLogin.error = emptyFieldError
                return false
            }
            return true
        }


    }


}