package com.minor_project.flaamandroid.ui.feed.post

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.data.request.AddUpdateImplementationRequest
import com.minor_project.flaamandroid.databinding.FragmentAddImplementationBinding
import com.minor_project.flaamandroid.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.widget.ArrayAdapter
import androidx.core.util.forEach
import timber.log.Timber


@AndroidEntryPoint
class AddImplementationFragment : Fragment() {


    private val args: PostDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddImplementationBinding

    private val viewModel: AddImplementationViewModel by viewModels()

    private var milestonesList: ArrayList<String> = ArrayList()

    private var milestonesListSha1Sum: ArrayList<String> = ArrayList()


    private var completedMilestonesList: ArrayList<String> = ArrayList()

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
        viewModel.getIdeaDetails(args.ideaId)
        viewModel.ideaDetails.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.llAddImplementation.visible()
                    makeToast(res.message.toString())
                }

                is ApiResponse.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.gone()
                    binding.llAddImplementation.visible()
                    milestonesList = arrayListOf()
                    res.body.milestones!!.forEach { milestone ->
                        milestonesListSha1Sum.add(milestone[0])
                        milestonesList.add(milestone[1])
                    }

                    binding.listViewMilestonesAddImplementation.choiceMode =
                        ListView.CHOICE_MODE_MULTIPLE

                    binding.listViewMilestonesAddImplementation.adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_multiple_choice, milestonesList
                    )

//                    val adapter = ArrayAdapter(
//                        requireContext(),
//                        com.minor_project.flaamandroid.R.layout.item_milestone_add_implementation,
//                        com.minor_project.flaamandroid.R.id.tv_milestone_item_milestone_add_implementation,
//                        milestonesList
//                    )


//                    val adapter =
//                        MilestoneAddImplementationAdapter(milestonesList, requireContext())
//                    binding.listViewMilestonesAddImplementation.adapter = adapter


                    binding.apply {
                        btnAddImplementation.setOnClickListener {
                            completedMilestonesList = arrayListOf()
                            binding.listViewMilestonesAddImplementation.checkedItemPositions.forEach { key, _ ->
                                completedMilestonesList.add(milestonesListSha1Sum[key])
                            }


                            if (validate()) {
                                Timber.e("COMPLETED MILESTONES" + completedMilestonesList)
                                viewModel.addImplementation(
                                    AddUpdateImplementationRequest(
                                        etAddBodyAddImplementation.text.toString(),
                                        completedMilestonesList,
                                        etAddOverviewDescriptionAddImplementation.text.toString(),
                                        true,
                                        args.ideaId,
                                        null,
                                        null,
                                        etGithubRepoLinkAddImplementation.text.toString(),
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
        val enterValidUrl = "Please enter a Valid Url!"
        binding.apply {
            if (etAddTitleAddImplementation.text.isNullOrEmpty()) {
                etAddTitleAddImplementation.error = emptyFieldError
                return false
            }

            if (!etGithubRepoLinkAddImplementation.text.isNullOrEmpty()) {
                return if (Patterns.WEB_URL.matcher(etGithubRepoLinkAddImplementation.text.toString())
                        .matches()
                ) {
                    makeToast("true")
                    true
                } else {
                    makeToast("false")
                    etGithubRepoLinkAddImplementation.error = enterValidUrl
                    false
                }
            }

            return true
        }
    }


}