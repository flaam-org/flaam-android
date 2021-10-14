package com.minor_project.flaamandroid.ui.feed.post

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.AddImplementationRequest
import com.minor_project.flaamandroid.data.request.PostIdeaRequest
import com.minor_project.flaamandroid.databinding.FragmentAddImplementationBinding
import com.minor_project.flaamandroid.databinding.FragmentPostIdeaBinding
import com.minor_project.flaamandroid.ui.feed.PostIdeaViewModel
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddImplementationFragment : Fragment() {


    private val args: PostDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddImplementationBinding

    private val viewModel: AddImplementationViewModel by viewModels()

    @Inject
    lateinit var preferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImplementationBinding.inflate(inflater)
        initObservers()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.ideaId
        makeToast(id.toString())
    }

    private fun initObservers() {
        viewModel.getUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast("Unable to fetch Data!")
                }

                is ApiResponse.Success -> {

                    val ownerId = it.body.id

                    binding.apply {
                        btnAddImplementation.setOnClickListener {
                            if (validate()) {
                                viewModel.addImplementation(
                                    AddImplementationRequest(
                                        null,
                                        null,
                                        etAddOverviewDescriptionAddImplementation.text.toString(),
                                        false,
                                        args.ideaId,
                                        null,
                                        null,
                                        ownerId,
                                        null,
                                        etAddTitleAddImplementation.text.toString()
                                    )
                                )
                            } else {
                                makeToast("Missing Required Fields!")
                            }

                        }
                    }

                }
            }
        }

        viewModel.addImplementation.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.message.toString())
                }
                is ApiResponse.Success -> {
                    makeToast("Successfully Added Implementation!")
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        binding.apply {
            if (etAddTitleAddImplementation.text.isNullOrEmpty()) {
                etAddTitleAddImplementation.error = emptyFieldError
                return false
            }

            if (etGithubRepoLinkAddImplementation.text.isNullOrEmpty()) {
                etGithubRepoLinkAddImplementation.error = emptyFieldError
                return false
            }

            return true
        }
    }


}