package com.kakai.android.drawingview.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Xfermode
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

    data class Triangle(
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
            val v1x = startX + (endX - startX) / 2
            val v1y = startY

            val v2x = startX
            val v2y = endY

            val v3x = endX
            val v3y = endY

            val points = floatArrayOf(
                v1x, v1y, v2x, v2y, v2x, v2y, v3x, v3y, v3x, v3y, v1x, v1y
            )
            canvas.drawLines(points, paint)
        }
    }

    data class Options(
        val mode: Xfermode?,
        @ColorInt val color: Int,
        val size: Float
    )
}