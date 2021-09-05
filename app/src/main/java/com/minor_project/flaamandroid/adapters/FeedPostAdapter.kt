package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.models.FeedPostModel

open class FeedPostAdapter(
    private val context : Context,
    private var list : ArrayList<FeedPostModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    var bookmark = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_feed_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.findViewById<ImageView>(R.id.civ_feed_post_user_image).setImageResource(model.userImage)
            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_title).text = model.title

            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_votes).text = model.votes.toString()
            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_implementations).text = model.implementations.toString()


            holder.itemView.findViewById<TextView>(R.id.tv_feed_post_description).text = model.description

            holder.itemView.findViewById<ImageView>(R.id.iv_bookmark).setOnClickListener {
                toggleBookmark(holder.itemView.findViewById<ImageView>(R.id.iv_bookmark))
            }

            holder.itemView.findViewById<ImageView>(R.id.iv_share).setOnClickListener {
                shareIdea(holder.itemView.findViewById<TextView>(R.id.tv_feed_post_title), holder.itemView.findViewById<TextView>(R.id.tv_feed_post_description))
            }
        }
    }

    private fun shareIdea(title : TextView, description: TextView) {

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, title.text)
            .putExtra(Intent.EXTRA_TEXT, description.text)

        context.startActivity(intent)

    }

    private fun toggleBookmark(ivBookmark : ImageView) {
        if(!bookmark)
        {
            ivBookmark.setImageResource(R.drawable.ic_bookmark_check)
            Toast.makeText(this.context, "Idea Added to Bookmarks", Toast.LENGTH_SHORT).show()
            bookmark = true
        }
        else
        {
            ivBookmark.setImageResource(R.drawable.ic_bookmark_uncheck)
            Toast.makeText(this.context, "Idea Removed from Bookmarks", Toast.LENGTH_SHORT).show()
            bookmark = false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}