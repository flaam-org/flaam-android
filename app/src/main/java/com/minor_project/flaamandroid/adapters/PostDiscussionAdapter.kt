package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.ItemDiscussionBinding
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostDiscussionFragment

class PostDiscussionAdapter(
    private val fragment: PostDiscussionFragment,
    private val context: Context,
    var list: ArrayList<DiscussionsResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            ItemDiscussionBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        (holder as MyViewHolder).binding.apply {

            tvDiscussionTitle.text = model.title
            tvDiscussionBody.text = model.body
            //todo add the votes count here (upvote - downvote)
//            tvUpvoteDownvoteDiscussion.text = (model.vote ?: 0).toString()
            when (model.vote) {
                -1 -> {
                    ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_filled_24dp)
                }

                1 -> {
                    ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_filled_24dp)
                }

                else -> {
                    ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_outline_24dp)
                    ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_outline_24dp)
                }
            }

            ivUpvoteDiscussion.setOnClickListener {
                ivUpvoteDiscussion.toggleVote(1, model)
            }
            ivDownvoteDiscussion.setOnClickListener {
                ivDownvoteDiscussion.toggleVote(-1, model)
            }

        }

    }


    override fun getItemCount(): Int = list.size


    interface OnClickListener {
        fun onClick(ideaId: Int, model: IdeasResponse.Result)
    }

    fun addToList(discussions: ArrayList<DiscussionsResponse.Result>) {
        list.addAll(discussions)
        notifyDataSetChanged()
    }

    fun setToList(discussions: ArrayList<DiscussionsResponse.Result>) {
        list = discussions
        notifyDataSetChanged()
    }


    private fun ImageView.toggleVote(vote: Int, model: DiscussionsResponse.Result) {
        if (vote == 1) {
            this.setImageResource(R.drawable.ic_upvote_filled_24dp)
            fragment.upvoteDiscussion(model.id!!)
        } else if (vote == -1) {
            this.setImageResource(R.drawable.ic_downvote_filled_24dp)
            fragment.downvoteDiscussion(model.id!!)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    private class MyViewHolder(val binding: ItemDiscussionBinding) :
        RecyclerView.ViewHolder(binding.root)
}