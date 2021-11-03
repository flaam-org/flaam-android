package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kofigyan.stateprogressbar.StateProgressBar
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.databinding.ItemDiscussionBinding
import com.minor_project.flaamandroid.databinding.ItemMyImplementationsBinding
import com.minor_project.flaamandroid.ui.userprofile.tabs.MyImplementationsFragment


class MyImplementationsAdapter(
    private val fragment: MyImplementationsFragment,
    private val context: Context,
    var list: ArrayList<ImplementationsResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            ItemMyImplementationsBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        (holder as MyViewHolder).binding.apply {
            civOwnerUserImageMyImplementations.setOwnerAvatar(model.ownerAvatar.toString())
            tvOwnerNameMyImplementations.text = model.ownerUsername.toString()

            tvTitleMyImplementations.text = model.title.toString()

            val upvote = model.upvoteCount ?: 0
            val downvote = model.downvoteCount ?: 0
            val votes = upvote - downvote
            tvUpvoteDownvoteMyImplementations.text = votes.toString()


            updateViewForVote(model.vote!!, model)

            tvDescriptionMyImplementations.text = model.description.toString()
            val milestonesCount = model.milestones!!.size

            ivGithubMyImplementations.setOnClickListener {
                fragment.openRepository(model.repoUrl.toString())
            }
        }

    }


    override fun getItemCount(): Int = list.size


    interface OnClickListener {
        fun onClick(ideaId: Int, model: IdeasResponse.Result)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    fun addToList(implementations: ArrayList<ImplementationsResponse.Result>) {
        list.addAll(implementations)
        notifyDataSetChanged()
    }

    fun setToList(implementations: ArrayList<ImplementationsResponse.Result>) {
        list = implementations
        notifyDataSetChanged()
    }

    private fun ImageView.setOwnerAvatar(ownerAvatar: String) {

        fragment.setOwnerAvatar(ownerAvatar, this@setOwnerAvatar)
    }

    private fun ItemMyImplementationsBinding.updateViewForVote(
        vote: Int,
        model: ImplementationsResponse.Result
    ) {

        ivDownvoteMyImplementations.setImageResource(R.drawable.ic_downvote_outline_24dp)
        ivUpvoteMyImplementations.setImageResource(R.drawable.ic_upvote_outline_24dp)

        when (vote) {
            -1 -> {
                ivDownvoteMyImplementations.setImageResource(R.drawable.ic_downvote_filled_24dp)
            }

            1 -> {
                ivUpvoteMyImplementations.setImageResource(R.drawable.ic_upvote_filled_24dp)
            }
        }

        ivUpvoteMyImplementations.setOnClickListener {
            if (vote == 1) {
                toggleVote(0, model)
            } else {
                toggleVote(1, model)
            }

        }
        ivDownvoteMyImplementations.setOnClickListener {
            if (vote == -1) {
                toggleVote(0, model)
            } else {
                toggleVote(-1, model)
            }

        }
    }

    private fun toggleVote(vote: Int, model: ImplementationsResponse.Result) {
        fragment.voteImplementation(model.id!!, vote)
    }


    private class MyViewHolder(val binding: ItemMyImplementationsBinding) :
        RecyclerView.ViewHolder(binding.root)
}