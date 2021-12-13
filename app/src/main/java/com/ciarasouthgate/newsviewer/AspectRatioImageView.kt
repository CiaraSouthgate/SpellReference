package com.ciarasouthgate.newsviewer

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioImageView(context: Context) : AppCompatImageView(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width * drawable.intrinsicHeight / drawable.intrinsicWidth
        setMeasuredDimension(width, height)
    }
}