package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.data.response.TagsResponse
import com.minor_project.flaamandroid.databinding.ItemUserTagBinding
import com.minor_project.flaamandroid.ui.userprofile.EditProfileFragment


open class UserFavouriteTagsAdapter(
    private val fragment: EditProfileFragment,
    private val context: Context,
    private var list: ArrayList<TagsResponse.Result>
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
                fragment.removeFromFavouriteTags(model.id!!)
            }
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


    fun addToList(ideas: ArrayList<TagsResponse.Result>) {
        list.addAll(ideas)
        notifyDataSetChanged()
    }

    fun setToList(ideas: ArrayList<TagsResponse.Result>) {
        list = ideas
        notifyDataSetChanged()
    }

    private class TagsViewHolder(val binding: ItemUserTagBinding) :
        RecyclerView.ViewHolder(binding.root)

}