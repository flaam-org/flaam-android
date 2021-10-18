package com.minor_project.flaamandroid.adapters

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragManageAdapter(
    adapter: MilestonesAdapter,
    context: Context,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    var milestonesAdapter = adapter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        milestonesAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }


}