package com.minor_project.flaamandroid.ui.reset_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {


    private lateinit var binding: FragmentResetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater)
        // Inflate the layout for this fragment



        return binding.root
    }

}