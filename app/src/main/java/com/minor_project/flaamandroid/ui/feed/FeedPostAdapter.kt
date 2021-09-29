package com.minor_project.flaamandroid.ui.feed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.ItemFeedPostBinding
import com.minor_project.flaamandroid.utils.*
import timber.log.Timber


open class FeedPostAdapter(
    private val context: Context,
    var list: ArrayList<IdeasResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null
    var isEndReached = false



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == Constants.PROGRESS_VIEW){
            context.getProgressViewHolder()
        }else{
            MyViewHolder(
                ItemFeedPostBinding.inflate(LayoutInflater.from(context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position != list.size) {


            val model = list[position]

            var bookmark = false


            (holder as MyViewHolder).binding.apply {
                holder.itemView.findViewById<ImageView>(R.id.civ_feed_post_user_image)
                    .setImageResource(R.drawable.ic_profile_image_place_holder)

                tvFeedPostTitle.text = model.title
                tvFeedPostVotes.text = (model.vote ?: "0").toString()
                tvFeedPostDescription.text = model.description



                val cgFeedPostTags = cgFeedPostTags

                val tagsList = list[position].tags ?: emptyList()

                Timber.e("tags" + tagsList)

                cgFeedPostTags.removeAllViews()
                for (tag in tagsList) {
                    val chip = Chip(context)
                    chip.text = tag?.name
                    cgFeedPostTags.addView(chip)
                }

                ivBookmark.setOnClickListener {
                        ivBookmark.toggleBookmark(bookmark)
                        bookmark = bookmark.not()
                    }

                ivShare.setOnClickListener {
                        shareIdea(
                            tvFeedPostTitle,
                            tvFeedPostDescription
                        )
                    }

                holder.itemView.setOnClickListener {

                    if (onClickListener != null) {
                        onClickListener!!.onClick(position, model)
                    }

                }
            }

        }else{
            (holder as ProgressViewHolder).binding.apply{
                if(isEndReached){
                    llTvProgress.gone()
                    llTvEnd.visible()
                }else{
                    llTvProgress.visible()
                    llTvEnd.gone()
                }
            }

        }
    }


    override fun getItemCount(): Int = list.size + 1

    override fun getItemViewType(position: Int): Int = if(position == list.size) 1 else 0

    interface OnClickListener {
        fun onClick(position: Int, model: IdeasResponse.Result)
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

    private fun ImageView.toggleBookmark(bookmark: Boolean) {
        if (!bookmark) {
            this.setImageResource(R.drawable.ic_bookmark_check)
            Toast.makeText(this.context, "Idea Added to Bookmarks", Toast.LENGTH_SHORT).show()

        } else {
            this.setImageResource(R.drawable.ic_bookmark_uncheck)
            Toast.makeText(this.context, "Idea Removed from Bookmarks", Toast.LENGTH_SHORT).show()

        }
    }

    fun addToList(ideas: ArrayList<IdeasResponse.Result>){
        list.addAll(ideas)
        notifyDataSetChanged()
    }

    private class MyViewHolder(val binding: ItemFeedPostBinding) : RecyclerView.ViewHolder(binding.root)
}