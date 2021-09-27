package com.minor_project.flaamandroid.ui.feed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.minor_project.flaamandroid.data.response.IdeaResponseItem
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import timber.log.Timber


open class FeedPostAdapter(
    private val feedFragment: FeedFragment,
    private val context: Context,
    private var list: ArrayList<IdeaResponseItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null
    var bookmark = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                com.minor_project.flaamandroid.R.layout.item_feed_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.findViewById<ImageView>(R.id.civ_feed_post_user_image)
                .setImageResource(R.drawable.ic_profile_image_place_holder)
            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_title).text =
                model.title

            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_votes).text =
                model.vote.toString()

            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_description).text =
                model.description


            val cgFeedPostTags = holder.itemView.findViewById<ChipGroup>(R.id.cg_feed_post_tags)

            val tagsList = feedFragment.getTagsListFromIds(list[position].tags)

            Timber.e("tags" + tagsList)

            for (tag in tagsList) {
                val chip = Chip(context)
                chip.text = tag
                cgFeedPostTags.addView(chip)
            }

            holder.itemView.findViewById<ImageView>(R.id.iv_bookmark)
                .setOnClickListener {
                    toggleBookmark(holder.itemView.findViewById<ImageView>(R.id.iv_bookmark))
                }

            holder.itemView.findViewById<ImageView>(R.id.iv_share)
                .setOnClickListener {
                    shareIdea(
                        holder.itemView.findViewById<TextView>(R.id.tv_feed_post_title),
                        holder.itemView.findViewById<TextView>(R.id.tv_feed_post_description)
                    )
                }

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }

            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: IdeaResponseItem)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    private fun shareIdea(title: TextView, description: TextView) {

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, title.text)
            .putExtra(Intent.EXTRA_TEXT, description.text)

        context.startActivity(intent)

    }

    private fun toggleBookmark(ivBookmark: ImageView) {
        if (!bookmark) {
            ivBookmark.setImageResource(R.drawable.ic_bookmark_check)
            Toast.makeText(this.context, "Idea Added to Bookmarks", Toast.LENGTH_SHORT).show()
            bookmark = true
        } else {
            ivBookmark.setImageResource(R.drawable.ic_bookmark_uncheck)
            Toast.makeText(this.context, "Idea Removed from Bookmarks", Toast.LENGTH_SHORT).show()
            bookmark = false
        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}