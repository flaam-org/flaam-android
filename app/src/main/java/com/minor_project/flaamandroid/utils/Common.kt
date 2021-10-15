package com.minor_project.flaamandroid.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.databinding.ProgressRecyclerItemBinding
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private lateinit var mProgressDialog: Dialog

fun <T> handleGetResponse(response: Response<T>): ApiResponse<T> {
    if (response.code() == 200) {
        response.body().let {
            Timber.e(response.body().toString())
            return ApiResponse.Success(it!!)
        }
    }

    Timber.e(response.errorBody()?.string().toString())
    return ApiResponse.Error(message = response.message(), status = response.code())
}


fun <T> handlePostResponse(response: Response<T>): ApiResponse<T> {
    if (response.code() == 201) {
        response.body().let {
            Timber.e(response.body().toString())
            return ApiResponse.Success(it!!)
        }
    }

    return ApiResponse.Error(
        message = response.errorBody()?.string().toString(),
        status = response.code()
    )
}


fun String.getDaysDiff(): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    val date = sdf.parse(this.substring(0, this.length - 13) + this.substring(this.length - 6))
    val today = Date()

    val diff = (((date!!.time - today.time) / (1000 * 60 * 60 * 24) % 7)).toInt()
    return abs(diff)

}

class ProgressViewHolder(val binding: ProgressRecyclerItemBinding) :
    RecyclerView.ViewHolder(binding.root)

fun Context.getProgressViewHolder(): ProgressViewHolder {
    return ProgressViewHolder(
        ProgressRecyclerItemBinding.inflate(LayoutInflater.from(this))
    )
}


fun ImageView.loadImage(image: String) {
    Glide.with(this).load(image).centerCrop().into(this)
}

fun Context.showProgressDialog() {
    mProgressDialog = Dialog(this)

    /*Set the screen content from a layout resource.
    The resource will be inflated, adding all top-level views to the screen.*/
    mProgressDialog.setContentView(R.layout.dialog_progress)

    //Start the dialog and display it on screen.
    mProgressDialog.show()
}

fun Context.hideProgressDialog() {
    mProgressDialog.dismiss()
}

val listOfChipColors = arrayListOf(
    Color.parseColor("#4CE899"),
    Color.parseColor("#FFD400"),
    Color.parseColor("#CC2A27"),
    Color.parseColor("#6200EE")
)