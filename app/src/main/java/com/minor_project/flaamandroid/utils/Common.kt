package com.minor_project.flaamandroid.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.response.IdeasResponse
import com.minor_project.flaamandroid.databinding.ProgressRecyclerItemBinding
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber
import java.text.ParseException
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
    return ApiResponse.Error(
        message = response.message(),
        body = response.body(),
        status = response.code()
    )
}


fun <T> handlePostResponse(response: Response<T>): ApiResponse<T> {
    if (response.code() == 201) {
        response.body().let {
            Timber.e(response.body().toString())
            return ApiResponse.Success(it!!)
        }
    }


    Timber.e(response.errorBody()?.string().toString())
    return ApiResponse.Error(
        message = response.errorBody()?.string().toString(),
        body = response.body(),
        status = response.code()
    )
}


fun String.getDaysDiff(): Int {

    Timber.e("$this")

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
    val diff: Int = try {
        val date = sdf.parse(this)
        val today = Date()

        (((date!!.time - today.time) / (1000 * 60 * 60 * 24) % 7)).toInt()
    } catch (e: ParseException) {
        e.printStackTrace()
        0
    }

    return abs(diff)

}

class ProgressViewHolder(val binding: ProgressRecyclerItemBinding) :
    RecyclerView.ViewHolder(binding.root)

fun Context.getProgressViewHolder(): ProgressViewHolder {
    return ProgressViewHolder(
        ProgressRecyclerItemBinding.inflate(LayoutInflater.from(this))
    )
}


@DelicateCoroutinesApi
fun ImageView.loadImage(image: String) {
    val imageLoader = ImageLoader.Builder(this.context).build()
    val request = ImageRequest.Builder(this.context)
        .data(image)
        .build()

    GlobalScope.launch(Dispatchers.Main) {
        val drawable = imageLoader.execute(request).drawable
        this@loadImage.setImageDrawable(drawable)
    }

}


suspend fun ImageView.loadSVG(image: String) {

    val context = this@loadSVG.context
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    val request = ImageRequest.Builder(context)
        .data(image)
        .build()

    val drawable = imageLoader.execute(request).drawable

    withContext(Dispatchers.Main) {
        this@loadSVG.setImageDrawable(drawable)
    }

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


fun Context.progressDialog(): Dialog {
    val progressDialog = Dialog(this@progressDialog)
    progressDialog.setContentView(R.layout.dialog_progress)

    return progressDialog
}

fun PopupWindow.showPopupDimBehind() {

    val container = this.contentView.rootView
    val wm = container.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val p = container.layoutParams as WindowManager.LayoutParams
    p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
    p.dimAmount = 0.5f
    wm.updateViewLayout(container, p)
}

fun View.showRemainingTagsPopup(subList: List<IdeasResponse.Result.Tag?>) {
    val menuPopup = PopupMenu(context, this, Gravity.CENTER)
    subList.forEach {
        menuPopup.menu.add(it?.name.toString())
    }
    menuPopup.show()
}

val listOfChipColors = arrayListOf(
    Color.parseColor("#4CE899"),
    Color.parseColor("#FFD400"),
    Color.parseColor("#CC2A27"),
    Color.parseColor("#6200EE")
)