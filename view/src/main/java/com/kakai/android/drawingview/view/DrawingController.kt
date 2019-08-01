package com.kakai.android.drawingview.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

internal class DrawingController {

    private val ops = mutableListOf<DrawingOp>()

    private var lastX: Float? = null
    private var lastY: Float? = null
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
            op.render(canvas, paint)
        }

        // Restore current paint options
        paint.drawingOptions = currentDrawingOptions

        currentOp?.render(canvas, paint)
    }

    fun onTouchDown(x: Float, y: Float) {
        lastX = x
        lastY = y

        currentOp = when (mode) {
            is DrawingMode.Path -> {
                DrawingOp.Path(
                    options = paint.drawingOptions,
                    path = Path().apply {
                        moveTo(x, y)
                    }
                )
            }
            is DrawingMode.Rectangle -> {
                DrawingOp.Rectangle(
                    options = paint.drawingOptions,
                    startX = x,
                    startY = y,
                    endX = x,
                    endY = y
                )
            }
            is DrawingMode.Oval -> {
                DrawingOp.Oval(
                    options = paint.drawingOptions,
                    startX = x,
                    startY = y,
                    endX = x,
                    endY = y
                )
            }
        }
    }

    fun onTouchUp() {
        lastX = null
        lastY = null
        currentOp?.let { ops.add(it) }
        currentOp = null
    }

    fun onTouchMove(x: Float, y: Float) {
        val lastX = lastX
        val lastY = lastY

        if (lastX != null && lastY != null) {
            currentOp?.update(lastX, lastY, x, y)
        }

        this.lastX = x
        this.lastY = y
    }

    var onModeChangedListener: ((DrawingMode) -> Unit)? = null

    var mode: DrawingMode = DrawingMode.Path
        set(value) {
            field = value
            onModeChangedListener?.invoke(value)
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
        lastX = null
        lastY = null
        currentOp = null
    }

    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
    }
}