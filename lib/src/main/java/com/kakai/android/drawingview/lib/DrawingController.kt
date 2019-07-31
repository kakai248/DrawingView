package com.kakai.android.drawingview.lib

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

internal class DrawingController {

    private val ops = mutableListOf<DrawingOp>()

    private var currentX: Float? = null
    private var currentY: Float? = null
    private var currentOp: DrawingOp? = null

    private val paint = Paint().apply {
        isAntiAlias = true
        color = DEFAULT_COLOR
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
    }

    fun render(canvas: Canvas) {
        // Save current paint options
        val currentDrawingOptions = paint.drawingOptions

        ops.forEach { op ->
            paint.drawingOptions = op.options
            renderOp(canvas, op)
        }

        // Restore current paint options
        paint.drawingOptions = currentDrawingOptions

        currentOp?.let { renderOp(canvas, it) }
    }

    private fun renderOp(canvas: Canvas, op: DrawingOp) {
        when (op) {
            is DrawingOp.Path -> {
                canvas.drawPath(op.path, paint)
            }
        }
    }

    fun onTouchDown(x: Float, y: Float) {
        currentX = x
        currentY = y
        currentOp = DrawingOp.Path(
            options = paint.drawingOptions,
            path = Path().apply {
                moveTo(x, y)
            }
        )
    }

    fun onTouchUp() {
        currentX = null
        currentY = null
        currentOp?.let { ops.add(it) }
        currentOp = null
    }

    fun onTouchMove(x: Float, y: Float) {
        val op = currentOp
        when (op) {
            is DrawingOp.Path -> {
                val currentX = currentX
                val currentY = currentY

                if (currentX != null && currentY != null) {
                    op.path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
                }

                this.currentX = x
                this.currentY = y
            }
        }
        currentOp = op
    }

    var color: Int
        get() = paint.color
        set(value) {
            paint.color = value
        }

    fun undo(): Boolean {
        return if (ops.isNotEmpty()) {
            ops.removeAt(ops.lastIndex)
            true
        } else false
    }

    fun reset() {
        ops.clear()
        currentX = null
        currentY = null
        currentOp = null
    }

    private var Paint.drawingOptions: DrawingOp.Options
        get() = DrawingOp.Options(
            color = color
        )
        set(value) {
            color = value.color
        }

    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
    }
}