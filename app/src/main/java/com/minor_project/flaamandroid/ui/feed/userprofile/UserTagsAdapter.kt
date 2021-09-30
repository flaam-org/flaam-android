package com.minor_project.flaamandroid.ui.feed.userprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.data.response.TagsResponseItem
import com.minor_project.flaamandroid.databinding.ItemUserTagBinding


open class UserTagsAdapter(

    private val context: Context,
    private var list: ArrayList<TagsResponseItem>,
    private val deleteTag: (id: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(context)
        return TagsViewHolder(ItemUserTagBinding.inflate(inflater))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is TagsViewHolder) {
            val model = list[position]
            holder.binding.tvUserTag.text = model.name



            holder.binding.ivDeleteUserTag.setOnClickListener {
                deleteTag.invoke(model.id!!)
            }
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    fun updateUserTagsList(newTagsList: ArrayList<TagsResponseItem>) {
        list = newTagsList
        notifyDataSetChanged()
    }

    private class TagsViewHolder(val binding: ItemUserTagBinding) :
        RecyclerView.ViewHolder(binding.root)

}