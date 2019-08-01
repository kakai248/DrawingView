package com.kakai.android.drawingview.view

import android.graphics.*
import java.util.*

internal class DrawingController {

    private val ops = LinkedList<DrawingOp>()

    private var lastX: Float? = null
    private var lastY: Float? = null
    private var currentOp: DrawingOp? = null

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    private val paint = Paint().apply {
        xfermode = null
        isAntiAlias = true
        color = DEFAULT_COLOR
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEFAULT_SIZE
    }

    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
    }

    fun render(canvas: Canvas) {
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        ops.forEach { op ->
            paint.drawingOptions = op.options
            op.render(bitmapCanvas, paint)
        }

        currentOp?.let { op ->
            paint.drawingOptions = op.options
            op.render(bitmapCanvas, paint)
        }

        canvas.drawColor(0xFFFFFF)
        canvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
    }

    fun onTouchDown(x: Float, y: Float) {
        lastX = x
        lastY = y

        currentOp = when (mode) {
            is DrawingMode.Eraser -> {
                DrawingOp.Path(
                    options = paint.drawingOptions.copy(
                        mode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                    ),
                    path = Path().apply {
                        moveTo(x, y)
                    }
                )
            }
            is DrawingMode.Path -> {
                DrawingOp.Path(
                    options = paint.drawingOptions.copy(
                        mode = null
                    ),
                    path = Path().apply {
                        moveTo(x, y)
                    }
                )
            }
            is DrawingMode.Rectangle -> {
                DrawingOp.Rectangle(
                    options = paint.drawingOptions.copy(
                        mode = null
                    ),
                    startX = x,
                    startY = y,
                    endX = x,
                    endY = y
                )
            }
            is DrawingMode.Oval -> {
                DrawingOp.Oval(
                    options = paint.drawingOptions.copy(
                        mode = null
                    ),
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
        onHistoryChangedListener?.invoke(ops)
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

    var onColorChangedListener: ((Int) -> Unit)? = null

    var color: Int
        get() = paint.drawingOptions.color
        set(value) {
            paint.drawingOptions = paint.drawingOptions.copy(
                color = value
            )
            onColorChangedListener?.invoke(value)
        }

    var onSizeChangedListener: ((Float) -> Unit)? = null

    var size: Float
        get() = paint.drawingOptions.size
        set(value) {
            paint.drawingOptions = paint.drawingOptions.copy(
                size = value
            )
            onSizeChangedListener?.invoke(value)
        }

    var onHistoryChangedListener: ((List<DrawingOp>) -> Unit)? = null

    val history: List<DrawingOp>
        get() = ops

    fun undo(): Boolean {
        return if (ops.isNotEmpty()) {
            ops.removeAt(ops.lastIndex)
            onHistoryChangedListener?.invoke(ops)
            true
        } else false
    }

    fun reset() {
        ops.clear()
        lastX = null
        lastY = null
        currentOp = null
        onHistoryChangedListener?.invoke(ops)
    }

    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
        private const val DEFAULT_SIZE = 10f
    }
}