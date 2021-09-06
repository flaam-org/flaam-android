package com.minor_project.flaamandroid.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Fragment.makeToast(msg: String){
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}

fun View.makeSnackBar(msg: String){
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
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
