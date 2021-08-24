package com.minor_project.flaamandroid.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        // Inflate the layout for this fragment
        return binding.root
    }

//    private fun setUpActionBar() {
//        val toolbarSignUpActivity = findViewById<Toolbar>(R.id.toolbar_sign_up_activity)
//        setSupportActionBar(toolbarSignUpActivity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
//        }
//
//        toolbarSignUpActivity.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }


}