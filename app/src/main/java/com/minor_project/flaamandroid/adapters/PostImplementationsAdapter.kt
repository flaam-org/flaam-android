package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
import com.minor_project.flaamandroid.databinding.ItemPostImplementationBinding
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostImplementationsFragment


class PostImplementationsAdapter(
    private val fragment: PostImplementationsFragment,
    private val context: Context,
    var list: ArrayList<ImplementationsResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            ItemPostImplementationBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        (holder as MyViewHolder).binding.apply {
            civOwnerUserImagePostImplementation.setOwnerAvatar(model.ownerAvatar.toString())
            tvOwnerNamePostImplementation.text = model.ownerUsername.toString()
            val upvote = model.upvoteCount ?: 0
            val downvote = model.downvoteCount ?: 0
            val votes = upvote - downvote
            tvUpvoteDownvotePostImplementation.text = votes.toString()

            updateViewForVote(model.vote!!, model)

            tvDescriptionPostImplementation.text = model.description.toString()
            tvBodyPostImplementation.text = model.body.toString()
            val milestonesCount = model.milestones!!.size
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

    private fun ItemPostImplementationBinding.updateViewForVote(
        vote: Int,
        model: ImplementationsResponse.Result
    ) {

        ivDownvotePostImplementation.setImageResource(R.drawable.ic_downvote_outline_24dp)
        ivUpvotePostImplementation.setImageResource(R.drawable.ic_upvote_outline_24dp)

        when (vote) {
            -1 -> {
                ivDownvotePostImplementation.setImageResource(R.drawable.ic_downvote_filled_24dp)
            }

            1 -> {
                ivUpvotePostImplementation.setImageResource(R.drawable.ic_upvote_filled_24dp)
            }
        }

        ivUpvotePostImplementation.setOnClickListener {
            if (vote == 1) {
                toggleVote(0, model)
            } else {
                toggleVote(1, model)
            }

        }
        ivDownvotePostImplementation.setOnClickListener {
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


    private class MyViewHolder(val binding: ItemPostImplementationBinding) :
        RecyclerView.ViewHolder(binding.root)
}