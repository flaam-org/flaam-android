package com.minor_project.flaamandroid.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.R
import java.util.*


class SplashFragment : Fragment(R.layout.fragment_intro_temp) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        findNavController().navigate(R.id.introFragment)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spash, container, false)
    }


}