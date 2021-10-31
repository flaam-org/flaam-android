package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.databinding.ItemDiscussionBinding
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostDiscussionFragment
import com.minor_project.flaamandroid.utils.makeToast

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
            val upvoteCount = model.upvoteCount ?: 0
            val downvoteCount = model.downvoteCount ?: 0
            val upvoteDownvoteCount = upvoteCount - downvoteCount
            tvUpvoteDownvoteDiscussion.text = upvoteDownvoteCount.toString()
            when (model.vote) {
                -1 -> {
                    ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_filled_24dp)
                    ivUpvoteDiscussion.isClickable = false
                    ivDownvoteDiscussion.isClickable = false
                }

                1 -> {
                    ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_filled_24dp)
                    ivUpvoteDiscussion.isClickable = false
                    ivDownvoteDiscussion.isClickable = false
                }

                else -> {
                    ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_outline_24dp)
                    ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_outline_24dp)

                    ivUpvoteDiscussion.setOnClickListener {
                        ivUpvoteDiscussion.toggleVote(1, model)
                        ivUpvoteDiscussion.isClickable = false
                        ivDownvoteDiscussion.isClickable = false
                    }
                    ivDownvoteDiscussion.setOnClickListener {
                        ivDownvoteDiscussion.toggleVote(-1, model)
                        ivUpvoteDiscussion.isClickable = false
                        ivDownvoteDiscussion.isClickable = false
                    }
                }
            }

            etAddCommentDiscussionItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(android.R.color.darker_gray))
                        tvPostCommentDiscussionItem.isClickable = false
                        tvPostCommentDiscussionItem.isFocusable = false
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                    }

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(android.R.color.darker_gray))
                        tvPostCommentDiscussionItem.isClickable = false
                        tvPostCommentDiscussionItem.isFocusable = false
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(android.R.color.darker_gray))
                        tvPostCommentDiscussionItem.isClickable = false
                        tvPostCommentDiscussionItem.isFocusable = false
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                        tvPostCommentDiscussionItem.setOnClickListener {
                            fragment.makeToast("clicked!!!!!!")
                            etAddCommentDiscussionItem.setText("")
                        }
                    }
                }

            })
        }

    }


    override fun getItemCount(): Int = list.size


    interface OnClickListener {
        fun onClick(discussionId: Int, model: DiscussionsResponse.Result)
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