package com.minor_project.flaamandroid.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.makeToast(msg: String){
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}
