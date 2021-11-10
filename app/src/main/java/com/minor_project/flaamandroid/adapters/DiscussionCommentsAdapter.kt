package com.minor_project.flaamandroid.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.minor_project.flaamandroid.data.response.CommentsForDiscussionResponse
import com.minor_project.flaamandroid.databinding.ItemCommentBinding
import com.minor_project.flaamandroid.utils.loadSVG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.text.Spannable

import android.text.style.BackgroundColorSpan

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.minor_project.flaamandroid.R
import timber.log.Timber


class DiscussionCommentsAdapter(private val comments: List<CommentsForDiscussionResponse.Comments?>,private val lifecycleCoroutineScope: LifecycleCoroutineScope) :
    RecyclerView.Adapter<DiscussionCommentsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = comments[position]
        holder.binding.apply {
            lifecycleCoroutineScope.launch(Dispatchers.IO) {
                civAvatarComments.loadSVG(comment?.ownerAvatar.toString())
            }

            tvUsernameComment.text = comment?.ownerUsername
            tvComment.getHighlightedText(comment?.body.toString())


        }
    }

    override fun getItemCount(): Int = comments.size

    private fun TextView.getHighlightedText(comment: String): Unit? {

        if(comment.split(" ").firstOrNull{ it.first().toString() == "@" }   == null){
            this.text = comment
            return null
        }

        val pairOfIndexes = arrayListOf<Pair<Int, Int>>()
                val splitList = comment.split(" ")

        splitList.indices.forEach {
            if(splitList[it].first().toString() == "@"){
                if(it != 0){
                    val startIndex = splitList.subList(0, it).joinToString("").length + it
                    val endIndex = startIndex + splitList[it].length
                    pairOfIndexes.add(Pair(startIndex, endIndex))
                }else{
                    val startIndex = 0
                    val endIndex = startIndex + splitList[it].length
                    pairOfIndexes.add(Pair(startIndex, endIndex))


                }
            }

        }

        val spannableString = SpannableString(comment)

        pairOfIndexes.forEach {
            spannableString.setSpan(
                BackgroundColorSpan(Color.parseColor("#006fc1")), it.first, it.second,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#ffffff")), it.first, it.second,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val clickableSpan = object: ClickableSpan(){
                override fun onClick(widget: View) {

                    Timber.e("clicked !")

                    findNavController().navigate(R.id.action_global_userProfileFragment, bundleOf(Pair("username", comment.substring(it.first + 1, it.second))) )

                }
            }

            this.movementMethod = LinkMovementMethod.getInstance()

            spannableString.setSpan(
                clickableSpan, it.first, it.second,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }


        this.text = spannableString
        return null
    }

}