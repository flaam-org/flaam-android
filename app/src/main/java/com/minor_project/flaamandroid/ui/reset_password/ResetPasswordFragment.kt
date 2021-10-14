package com.minor_project.flaamandroid.ui.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentResetPasswordEmailBinding

class ResetPasswordFragment : Fragment() {


    private lateinit var binding: FragmentResetPasswordEmailBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordEmailBinding.inflate(inflater)
        binding.etMailFragmentReset

        binding.btnLogin.setOnClickListener {
            viewModel.getResetPasswordToken(binding.btnLogin.text.toString())
        }



        return binding.root
    }

}