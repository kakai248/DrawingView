package com.kakai.android.drawingview.view

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt

typealias AndroidPath = android.graphics.Path

sealed class DrawingOp(open val options: Options) {
    abstract fun update(oldX: Float, oldY: Float, newX: Float, newY: Float)
    abstract fun render(canvas: Canvas, paint: Paint)

    data class Path(
        override val options: Options,
        val path: AndroidPath
    ) : DrawingOp(options) {
        override fun update(oldX: Float, oldY: Float, newX: Float, newY: Float) {
            path.quadTo(oldX, oldY, (newX + oldX) / 2, (newY + oldY) / 2)
        }

        override fun render(canvas: Canvas, paint: Paint) {
            canvas.drawPath(path, paint)
        }
    }

    data class Rectangle(
        override val options: Options,
        val startX: Float,
        val startY: Float,
        var endX: Float,
        var endY: Float
    ) : DrawingOp(options) {
        override fun update(oldX: Float, oldY: Float, newX: Float, newY: Float) {
            endX = newX
            endY = newY
        }

        override fun render(canvas: Canvas, paint: Paint) {
            canvas.drawRect(startX, startY, endX, endY, paint)
        }
    }

    data class Oval(
        override val options: Options,
        val startX: Float,
        val startY: Float,
        var endX: Float,
        var endY: Float
    ) : DrawingOp(options) {
        override fun update(oldX: Float, oldY: Float, newX: Float, newY: Float) {
            endX = newX
            endY = newY
        }

        override fun render(canvas: Canvas, paint: Paint) {
            canvas.drawOval(startX, startY, endX, endY, paint)
        }
    }

    data class Options(
        @ColorInt val color: Int
    )
}