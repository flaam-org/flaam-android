package com.minor_project.flaamandroid.ui.userprofile.tabs

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.AddUpdateImplementationRequest
import com.minor_project.flaamandroid.databinding.FragmentEditImplementationBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EditImplementationFragment : Fragment() {

    @Inject
    lateinit var preferences: UserPreferences

    private lateinit var binding: FragmentEditImplementationBinding
    private val viewModel: EditImplementationViewModel by viewModels()

    private val args: EditImplementationFragmentArgs by navArgs()

    private var milestonesList: ArrayList<String> = arrayListOf()
    private var milestonesListSha1Sum: ArrayList<String> = arrayListOf()
    private var completedMilestonesListSha1Sum: ArrayList<String> = arrayListOf()
    private var completedMilestonesListIndex: ArrayList<Int> = arrayListOf()


    private var finalCompletedMilestonesListSha1Sum: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditImplementationBinding.inflate(inflater)

        makeToast(args.implementationId.toString())

        initObservers()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
    }


    private fun initObservers() {
        val shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()
        viewModel.getImplementationDetails(args.implementationId)
        viewModel.getImplementationDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.llEditImplementation.visible()
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.llEditImplementation.visible()
                    val ideaId = it.body.idea
                    milestonesList = arrayListOf()
                    milestonesListSha1Sum = arrayListOf()
                    completedMilestonesListSha1Sum = arrayListOf()
                    completedMilestonesListIndex = arrayListOf()
                    finalCompletedMilestonesListSha1Sum = arrayListOf()

                    binding.apply {
                        if (!it.body.title.isNullOrEmpty()) {
                            etAddTitleEditImplementation.setText(it.body.title.toString())
                        }
                        if (!it.body.description.isNullOrEmpty()) {
                            etAddOverviewDescriptionEditImplementation.setText(it.body.description.toString())
                        }
                        if (!it.body.body.isNullOrEmpty()) {
                            etAddBodyEditImplementation.setText(it.body.body.toString())
                        }
                        if (!it.body.repoUrl.isNullOrEmpty()) {
                            etGithubRepoLinkEditImplementation.setText(it.body.repoUrl.toString())
                        }


                        it.body.milestones!!.forEach { milestone ->
                            milestonesListSha1Sum.add(milestone[0])
                            milestonesList.add(milestone[1])
                        }


                        listViewMilestonesEditImplementation.choiceMode =
                            ListView.CHOICE_MODE_MULTIPLE

                        listViewMilestonesEditImplementation.adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_multiple_choice, milestonesList
                        )

                        completedMilestonesListSha1Sum =
                            it.body.completedMilestones as ArrayList<String>


                        for ((index, value) in it.body.milestones.withIndex()) {
                            completedMilestonesListSha1Sum.forEach { completedMilestoneSha1Sum ->
                                if (completedMilestoneSha1Sum == value[0]) {
                                    completedMilestonesListIndex.add(index)
                                }
                            }
                        }


                        Timber.i("completedMilestonesListIndex $completedMilestonesListIndex")

                        listViewMilestonesEditImplementation.checkCompletedMilestones(
                            completedMilestonesListIndex
                        )

                        btnEditImplementation.setOnClickListener {
                            finalCompletedMilestonesListSha1Sum = arrayListOf()

                            for (index in 0..listViewMilestonesEditImplementation.count.minus(1)) {
                                if (listViewMilestonesEditImplementation.isItemChecked(index)) {
                                    finalCompletedMilestonesListSha1Sum.add(milestonesListSha1Sum[index])
                                }
                            }

                            Timber.e("finalCompletedMilestonesListSha1Sum $finalCompletedMilestonesListSha1Sum")


                            if (validate()) {
                                Timber.e("FINAL COMPLETED MILESTONES $finalCompletedMilestonesListSha1Sum")
                                viewModel.updateImplementation(
                                    args.implementationId,
                                    AddUpdateImplementationRequest(
                                        etAddBodyEditImplementation.text.toString(),
                                        finalCompletedMilestonesListSha1Sum,
                                        etAddOverviewDescriptionEditImplementation.text.toString(),
                                        true,
                                        ideaId,
                                        null,
                                        null,
                                        etGithubRepoLinkEditImplementation.text.toString(),
                                        etAddTitleEditImplementation.text.toString()
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

        viewModel.updateImplementation.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    makeToast(res.body.toString())
                }
                is ApiResponse.Success -> {
                    makeToast("Implementation Edited Successfully!")
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun ListView.checkCompletedMilestones(list: ArrayList<Int>) {
        list.forEach { completedMilestoneIndex ->
            this.setItemChecked(
                completedMilestoneIndex,
                true
            )
        }
    }

    private fun validate(): Boolean {
        val emptyFieldError = "This Field Can't Be Empty!"
        val enterValidUrl = "Please enter a Valid Url!"
        binding.apply {
            if (etAddTitleEditImplementation.text.isNullOrEmpty()) {
                etAddTitleEditImplementation.error = emptyFieldError
                return false
            }

            if (!etGithubRepoLinkEditImplementation.text.isNullOrEmpty()) {
                return if (Patterns.WEB_URL.matcher(etGithubRepoLinkEditImplementation.text.toString())
                        .matches()
                ) {
                    makeToast("true")
                    true
                } else {
                    makeToast("false")
                    etGithubRepoLinkEditImplementation.error = enterValidUrl
                    false
                }
            }

            return true
        }
    }
}