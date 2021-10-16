package com.minor_project.flaamandroid.ui.reset_password

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentResetPasswordEmailBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.hideKeyboard
import com.minor_project.flaamandroid.utils.isEmailValid
import com.minor_project.flaamandroid.utils.makeSnackBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {


    private lateinit var binding: FragmentResetPasswordEmailBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordEmailBinding.inflate(inflater)



        binding.apply {
            btnLogin.setOnClickListener {
                hideKeyboard()
                if(etMailFragmentReset.text.toString().isEmailValid()){
                    viewModel.getResetPasswordToken(etMailFragmentReset.text.toString())
                }

            }
            ivClossResetPass.setOnClickListener {
                findNavController().popBackStack()
            }


        }



        viewModel.resetPassResult.observe(viewLifecycleOwner){
            when(it){
                is ApiResponse.Error -> {
                    Timber.e(it.body.toString())
                    binding.root.makeSnackBar("Password Reset Link Has Been Sent To Your Mail!")
                }

//                is ApiResponse.Success -> TODO()
            }
        }

        return binding.root
    }

}