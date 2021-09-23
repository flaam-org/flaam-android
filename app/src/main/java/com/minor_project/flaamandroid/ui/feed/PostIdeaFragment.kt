package com.minor_project.flaamandroid.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.databinding.FragmentPostIdeaBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import com.minor_project.flaamandroid.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostIdeaFragment : Fragment() {

    private lateinit var binding: FragmentPostIdeaBinding

    private val viewModel: PostIdeaViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostIdeaBinding.inflate(inflater)


        viewModel.postIdea.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiResponse.Success -> {
                    makeToast("Idea Posted Successfully!")
                }
            }
        }
        binding.apply {

            ivPostIdeaClose.setOnClickListener {
                findNavController().popBackStack()
            }

            btnPostIdea.setOnClickListener {
                //TODO Add the functionality
                if (validate()) {
                    viewModel.postIdea(
                        PostIdeaRequest(
                            null, binding.etPostIdeaDescription.text.toString(),
                            null, null, binding.etPostIdeaTitle.text.toString()
                        )
                    )
                } else {
                    makeToast("Missing Required Fields!")
                }
            }
        }


        return binding.root
    }

    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if (etPostIdeaTitle.text.isNullOrEmpty()) {
                etPostIdeaTitle.error = emptyFieldError
                return false
            }

            if (etPostIdeaDescription.text.isNullOrEmpty()) {
                etPostIdeaDescription.error = emptyFieldError
                return false
            }
            return true
        }
    }

}