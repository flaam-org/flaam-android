package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.request.PostCommentRequest
import com.minor_project.flaamandroid.data.response.CommentsForDiscussionResponse
import com.minor_project.flaamandroid.data.response.DiscussionsResponse
import com.minor_project.flaamandroid.databinding.ItemDiscussionBinding
import com.minor_project.flaamandroid.ui.feed.post.tabs.PostDiscussionFragment
import com.minor_project.flaamandroid.utils.gone
import com.minor_project.flaamandroid.utils.makeToast
import com.minor_project.flaamandroid.utils.visible

class PostDiscussionAdapter(
    private val fragment: PostDiscussionFragment,
    private val context: Context,
    var list: ArrayList<DiscussionsResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null


    // hashmap of position of discussion and its comments.
    private var mapOfComments = hashMapOf<Int, List<CommentsForDiscussionResponse.Comments?>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            ItemDiscussionBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[holder.bindingAdapterPosition]
        (holder as MyViewHolder).binding.apply {


            if (mapOfComments.keys.contains(holder.bindingAdapterPosition)) {
                pbLoadComments.gone()
                tvLoadComs.gone()
                val adapter = DiscussionCommentsAdapter(
                    mapOfComments[holder.bindingAdapterPosition] ?: emptyList()
                )
                rvCommentsDiscussions.adapter = adapter
            }

            tvDiscussionTitle.text = model.title
            tvDiscussionBody.text = model.body
            val upvoteCount = model.upvoteCount ?: 0
            val downvoteCount = model.downvoteCount ?: 0
            val upvoteDownvoteCount = upvoteCount - downvoteCount
            tvUpvoteDownvoteDiscussion.text = upvoteDownvoteCount.toString()

            updateViewForVote(model.vote!!, model)

            tvLoadComs.setOnClickListener {
                pbLoadComments.visible()
                tvLoadComs.gone()
                fragment.getComments(model.id!!, holder.bindingAdapterPosition)
            }

            etAddCommentDiscussionItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.disablePostTextView()
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                    }

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.disablePostTextView()
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (etAddCommentDiscussionItem.text.isNullOrEmpty()) {
                        tvPostCommentDiscussionItem.disablePostTextView()
                    } else {
                        tvPostCommentDiscussionItem.setTextColor(context.getColor(R.color.secondaryDarkColor2))
                        tvPostCommentDiscussionItem.setOnClickListener {
                            fragment.makeToast("clicked!!!!!!")
                            val comment = etAddCommentDiscussionItem.text.toString()
                            val body = PostCommentRequest(comment, model.id)

                            pbLoadComments.visible()
                            tvLoadComs.gone()

                            fragment.postComment(body, holder.bindingAdapterPosition)
                            etAddCommentDiscussionItem.setText("")

                        }
                    }
                }

            })
        }

    }

    fun updateCommentsForDiscussion(
        comments: List<CommentsForDiscussionResponse.Comments?>,
        position: Int
    ) {
        mapOfComments[position] = comments
        notifyItemChanged(position)
    }

    private fun ItemDiscussionBinding.updateViewForVote(
        vote: Int,
        model: DiscussionsResponse.Result
    ) {

        ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_outline_24dp)
        ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_outline_24dp)

        when (vote) {
            -1 -> {
                ivDownvoteDiscussion.setImageResource(R.drawable.ic_downvote_filled_24dp)
            }

            1 -> {
                ivUpvoteDiscussion.setImageResource(R.drawable.ic_upvote_filled_24dp)
            }
        }

        ivUpvoteDiscussion.setOnClickListener {
            if (vote == 1) {
                toggleVote(0, model)
            } else {
                toggleVote(1, model)
            }

        }
        ivDownvoteDiscussion.setOnClickListener {
            if (vote == -1) {
                toggleVote(0, model)
            } else {
                toggleVote(-1, model)
            }

        }
    }

    private fun TextView.disablePostTextView() {
        this.setTextColor(context.getColor(android.R.color.darker_gray))
        this.isClickable = false
        this.isFocusable = false
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


    private fun toggleVote(vote: Int, model: DiscussionsResponse.Result) {
        fragment.voteDiscussion(model.id!!, vote)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    private class MyViewHolder(val binding: ItemDiscussionBinding) :
        RecyclerView.ViewHolder(binding.root)
}