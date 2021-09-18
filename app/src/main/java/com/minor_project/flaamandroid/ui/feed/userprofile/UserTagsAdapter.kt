package com.minor_project.flaamandroid.ui.feed.userprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.ItemUserTagBinding
import com.minor_project.flaamandroid.databinding.ItemUserAddTagBinding

const val IS_TAG_VIEW = 0
const val IS_ADD_TAG_VIEW = 1

open class UserTagsAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val showPopup: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        return if (viewType == IS_TAG_VIEW) {
            TagsViewHolder(ItemUserTagBinding.inflate(inflater))
        } else {
            AddTagViewHolder(ItemUserAddTagBinding.inflate(inflater))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == list.size) {
            // add tag view

            (holder as AddTagViewHolder).also {
                it.binding.root.setOnClickListener {
                    showPopup.invoke()

                }
            }

        } else {
            // tag view
            val model = list[position]
            (holder as TagsViewHolder).also {
                it.binding.tvUserTag.text = model
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) {
            IS_ADD_TAG_VIEW
        } else {
            IS_TAG_VIEW
        }
    }


    override fun getItemCount(): Int {
        return list.size + 1
    }


    fun updateUserTagsList(newTagsList: ArrayList<String>) {
        list = newTagsList
        notifyDataSetChanged()
    }


    private class TagsViewHolder(val binding: ItemUserTagBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class AddTagViewHolder(val binding: ItemUserAddTagBinding) :
        RecyclerView.ViewHolder(binding.root)

}