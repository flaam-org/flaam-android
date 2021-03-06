package com.minor_project.flaamandroid.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.ItemFeedPostBinding
import com.minor_project.flaamandroid.ui.userprofile.tabs.MyBookmarksFragment
import com.minor_project.flaamandroid.utils.listOfChipColors
import timber.log.Timber


class MyBookmarksAdapter(
    private val fragment: MyBookmarksFragment,
    private val context: Context,
    var list: ArrayList<IdeasResponse.Result>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            ItemFeedPostBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var bookmark: Boolean
        val model = list[position]

        (holder as MyViewHolder).binding.apply {


            civFeedPostUserImage.setOwnerAvatar(model.ownerAvatar.toString())
            tvFeedPostTitle.text = model.title
            val upvote = model.upvoteCount ?: 0
            val downvote = model.downvoteCount ?: 0
            val votes = upvote - downvote
            tvFeedPostVotes.text = votes.toString()
            tvFeedPostImplementations.text = (model.implementationCount ?: 0).toString()
            tvFeedPostDescription.text = model.description


            val cgFeedPostTags = cgFeedPostTags

            val tagsList = list[position].tags ?: emptyList()

            Timber.e("tags $tagsList")

            cgFeedPostTags.removeAllViews()
            tagsList.indices.forEach { i ->
                val chip = Chip(context)

                if (i < 4) {
                    chip.text = tagsList[i]?.name
                    chip.chipBackgroundColor = ColorStateList.valueOf(listOfChipColors[i])
                    chip.setTextColor(Color.WHITE)
                    cgFeedPostTags.addView(chip)
                }
                if (i == 4) {
                    chip.text = "+${tagsList.size - 4}"
                    chip.setOnClickListener {
                        it.showRemainingTagsPopup(tagsList.subList(4, tagsList.size))
                    }
                    cgFeedPostTags.addView(chip)
                }

            }


            if (model.bookmarked == true) {
                ivBookmarkFeedPost.setImageResource(R.drawable.ic_bookmark_check)
                bookmark = true
            } else {
                ivBookmarkFeedPost.setImageResource(R.drawable.ic_bookmark_uncheck)
                bookmark = false
            }

            ivBookmarkFeedPost.setOnClickListener {
                ivBookmarkFeedPost.toggleBookmark(bookmark, model)
                bookmark = bookmark.not()
            }

            ivShareFeedPost.setOnClickListener {
                shareIdea(
                    tvFeedPostTitle,
                    tvFeedPostDescription
                )
            }

            val ideaId = model.id!!

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(ideaId, model)
                }

            }
        }

    }

    fun View.showRemainingTagsPopup(subList: List<IdeasResponse.Result.Tag?>) {
        val menuPopup = PopupMenu(context, this, Gravity.CENTER)
        subList.forEach {
            menuPopup.menu.add(it?.name.toString())
        }
        menuPopup.show()
    }


    override fun getItemCount(): Int = list.size


    interface OnClickListener {
        fun onClick(ideaId: Int, model: IdeasResponse.Result)
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

    fun addToList(ideas: ArrayList<IdeasResponse.Result>) {
        list.addAll(ideas)
        notifyDataSetChanged()
    }

    fun setToList(ideas: ArrayList<IdeasResponse.Result>) {
        list = ideas
        notifyDataSetChanged()
    }

    private fun ImageView.toggleBookmark(bookmark: Boolean, model: IdeasResponse.Result) {
        if (!bookmark) {
            this.setImageResource(R.drawable.ic_bookmark_check)
            fragment.addToBookmark(model.id!!)
        } else {
            this.setImageResource(R.drawable.ic_bookmark_uncheck)
            fragment.removeBookmark(model.id!!)
        }
    }

    private fun ImageView.setOwnerAvatar(ownerAvatar: String) {

        fragment.setOwnerAvatar(ownerAvatar, this@setOwnerAvatar)
    }

    private class MyViewHolder(val binding: ItemFeedPostBinding) :
        RecyclerView.ViewHolder(binding.root)
}