package com.minor_project.flaamandroid.ui.userprofile.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.iterator
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.FragmentEditImplementationBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
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
    var completedMilestonesListIndex: ArrayList<Int> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditImplementationBinding.inflate(inflater)

        makeToast(args.implementationId.toString())

        initObservers()
        initOnClick()

        return binding.root
    }

    private fun initOnClick() {

    }

    private fun initObservers() {
        viewModel.getImplementationDetails(args.implementationId)

        viewModel.getImplementationDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    makeToast(it.message.toString())
                }

                is ApiResponse.Success -> {
                    milestonesList = arrayListOf()
                    milestonesListSha1Sum = arrayListOf()
                    completedMilestonesListSha1Sum = arrayListOf()
                    completedMilestonesListIndex = arrayListOf()

                    binding.apply {
                        etAddTitleEditImplementation.setText(it.body.title.toString())
                        etAddOverviewDescriptionEditImplementation.setText(it.body.description.toString())
                        etAddBodyEditImplementation.setText(it.body.body.toString())
                        etGithubRepoLinkEditImplementation.setText(it.body.repoUrl.toString())

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

                        makeToast(completedMilestonesListIndex.toString())
                        completedMilestonesListIndex.forEach { completedMilestoneIndex ->
                            listViewMilestonesEditImplementation.setItemChecked(
                                completedMilestoneIndex,
                                true
                            )
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
}