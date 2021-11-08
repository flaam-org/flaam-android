package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.minor_project.flaamandroid.R

class MilestoneAddImplementationAdapter(private val dataSet: ArrayList<String>, mContext: Context) :
    ArrayAdapter<String>(mContext, R.layout.item_milestone_add_implementation, dataSet) {
    private class ViewHolder {
        lateinit var tv: TextView
        lateinit var cb: CheckBox
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): String {
        return dataSet[position]
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        val result: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_milestone_add_implementation, parent, false)
            viewHolder.tv =
                convertView.findViewById(R.id.tv_milestone_item_milestone_add_implementation)

            viewHolder.cb =
                convertView.findViewById(R.id.cb_milestone_item_milestone_add_implementation)

            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }
        val item: String = getItem(position)
        viewHolder.tv.text = item
        viewHolder.cb.isChecked = true
        return result
    }
}