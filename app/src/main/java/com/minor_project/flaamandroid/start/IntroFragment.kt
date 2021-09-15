package com.minor_project.flaamandroid.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)


        binding.apply {
            btnRegisterIntro.setOnClickListener {
                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterFragment())
            }
            btnLoginIntro.setOnClickListener {
                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}