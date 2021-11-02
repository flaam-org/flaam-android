package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kofigyan.stateprogressbar.StateProgressBar
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.data.response.ImplementationsResponse
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
            val upvote = model.upvoteCount ?: 0
            val downvote = model.downvoteCount ?: 0
            val votes = upvote - downvote
            tvUpvoteDownvoteMyImplementations.text = votes.toString()

            tvDescriptionMyImplementations.text = model.description.toString()
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


    private class MyViewHolder(val binding: ItemMyImplementationsBinding) :
        RecyclerView.ViewHolder(binding.root)
}