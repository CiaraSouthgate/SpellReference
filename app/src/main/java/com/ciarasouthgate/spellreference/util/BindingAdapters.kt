package com.ciarasouthgate.spellreference.util

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(value: Boolean) {
    isVisible = value
}