package com.minor_project.flaamandroid.utils

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView


class SquareLinearLayout(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.LinearLayoutCompat(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}

class SquareCardView(context: Context, attrs: AttributeSet) :
    CardView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}