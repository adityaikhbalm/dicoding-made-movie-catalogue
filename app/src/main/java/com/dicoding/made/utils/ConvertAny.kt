package com.dicoding.made.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat

object ConvertAny {
    private val metrics = Resources.getSystem().displayMetrics

    fun convertDpToPixel(dp: Float) = (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

    fun Context.convertColor(color: Int) = ContextCompat.getColor(this,color)

    fun calculateNoOfColumns(columnWidthDp: Float): Int {
        val screenWidthDp = metrics.widthPixels / metrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}