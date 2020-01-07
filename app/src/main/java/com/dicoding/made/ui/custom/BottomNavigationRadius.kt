package com.dicoding.made.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.dicoding.made.utils.ConvertAny.convertDpToPixel
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationRadius(context: Context, attrs: AttributeSet?) : BottomNavigationView(context, attrs) {

    var mPath = Path()

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(mPath)
        super.draw(canvas)
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val r = RectF(0f, 0f, w.toFloat(), h.toFloat())
        val radius = convertDpToPixel(50f).toFloat()
        val cornerDimensions = floatArrayOf(radius,radius,radius,radius,0f,0f,0f,0f)

        mPath = Path()
        mPath.addRoundRect(
            r,
            cornerDimensions,
            Path.Direction.CW
        )
        mPath.close()
    }
}