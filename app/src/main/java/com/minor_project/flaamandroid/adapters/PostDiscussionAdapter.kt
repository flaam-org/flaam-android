package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            tvUpvoteDownvoteDiscussion.text = (model.vote ?: 0).toString()

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

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    private class MyViewHolder(val binding: ItemDiscussionBinding) :
        RecyclerView.ViewHolder(binding.root)
}