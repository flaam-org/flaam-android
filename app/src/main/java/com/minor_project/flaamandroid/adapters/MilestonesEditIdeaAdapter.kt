package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.databinding.ItemMilestoneBinding
import com.minor_project.flaamandroid.ui.userprofile.tabs.EditIdeaFragment
import java.util.ArrayList

open class MilestonesEditIdeaAdapter(
    private val fragment: EditIdeaFragment,
    private val context: Context,
    private var list: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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


    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                list[i] = list.set(i + 1, list[i])
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                list[i] = list.set(i - 1, list[i])
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }


    private class MyViewHolder(
        val binding: ItemMilestoneBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}