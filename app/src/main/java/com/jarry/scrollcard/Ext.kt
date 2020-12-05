package com.jarry.scrollcard

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getCompatColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}