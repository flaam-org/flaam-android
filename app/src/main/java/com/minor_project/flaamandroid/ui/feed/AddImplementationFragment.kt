package com.minor_project.flaamandroid.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.databinding.FragmentAddImplementationBinding
import com.minor_project.flaamandroid.utils.makeToast


class AddImplementationFragment : Fragment() {

    private lateinit var binding: FragmentAddImplementationBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImplementationBinding.inflate(inflater)

        binding.apply {
            btnAddImplementation.setOnClickListener {

                //TODO add functionality
                makeToast("Your Implementation is Added Successfully!")
                findNavController().popBackStack()
            }
        }

        return binding.root

    }

}