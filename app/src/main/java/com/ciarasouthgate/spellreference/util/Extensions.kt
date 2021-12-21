package com.ciarasouthgate.spellreference.util

import android.content.res.Resources
import com.ciarasouthgate.spellreference.R

fun Int.toLevelOrdinal(resources: Resources): String =
    resources.getStringArray(R.array.ordinals)[this]