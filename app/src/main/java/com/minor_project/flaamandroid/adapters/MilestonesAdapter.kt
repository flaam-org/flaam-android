package com.minor_project.flaamandroid.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.ItemMilestoneBinding
import com.minor_project.flaamandroid.ui.feed.PostIdeaFragment
import com.minor_project.flaamandroid.utils.Constants
import java.util.*

open class MilestonesAdapter(
    private val fragment: PostIdeaFragment,
    private val context: Context,
    private var list: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mPositionDraggedFrom = -1
    private var mPositionDraggedTo = -1

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            ItemMilestoneBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val model = list[position]

        (holder as MyViewHolder).binding.apply {
            tvMilestoneItemMilestone.text = model

            ivDeleteMilestoneItemMilestone.setOnClickListener {
                deleteMilestone(model)
            }
        }

    }

    private fun deleteMilestone(model: String) {
        fragment.deleteMilestone(model)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private class MyViewHolder(
        val binding: ItemMilestoneBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}