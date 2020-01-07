package com.dicoding.made.ui.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.made.utils.ConvertAny

internal class ItemOffsetDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(
            ConvertAny.convertDpToPixel(10f), 0, 0, ConvertAny.convertDpToPixel(15f)
        )
    }
}