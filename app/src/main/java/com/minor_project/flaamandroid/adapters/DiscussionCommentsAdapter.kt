package com.minor_project.flaamandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.data.response.CommentsForDiscussionResponse
import com.minor_project.flaamandroid.databinding.ItemCommentBinding
import com.minor_project.flaamandroid.databinding.ItemDiscussionBinding
import com.minor_project.flaamandroid.utils.loadSVG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiscussionCommentsAdapter(private val comments: List<CommentsForDiscussionResponse.Comments?>): RecyclerView.Adapter<DiscussionCommentsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = comments[position]
        holder.binding.apply {
            GlobalScope.launch {
                ivAvatarComments.loadSVG(comment?.ownerAvatar.toString())
            }

            tvUsernameComment.text = comment?.ownerUsername
            tvComment.text = comment?.body


        }
    }

    override fun getItemCount(): Int = comments.size

}