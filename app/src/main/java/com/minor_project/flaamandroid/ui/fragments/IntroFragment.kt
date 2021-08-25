package com.minor_project.flaamandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentIntroTempBinding


class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroTempBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroTempBinding.inflate(inflater)


        binding.apply {
            btnRegisterIntro.setOnClickListener {
                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToSignUpFragment())
            }
            btnSignInIntro.setOnClickListener {
                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}