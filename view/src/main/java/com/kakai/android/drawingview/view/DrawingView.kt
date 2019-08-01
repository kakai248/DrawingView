package com.kakai.android.drawingview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView : View {

    private val controller = DrawingController()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        controller.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        controller.render(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                controller.onTouchDown(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                controller.onTouchUp()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                controller.onTouchMove(event.x, event.y)
                invalidate()
            }
        }

        return true
    }

    fun setOnModeChangedListener(func: (DrawingMode) -> Unit) {
        controller.onModeChangedListener = func
    }

    var mode: DrawingMode
        get() = controller.mode
        set(value) {
            controller.mode = value
        }

    fun setOnColorChangedListener(func: (Int) -> Unit) {
        controller.onColorChangedListener = func
    }

    var color: Int
        get() = controller.color
        set(value) {
            controller.color = value
        }

    fun setOnSizeChangedListener(func: (Float) -> Unit) {
        controller.onSizeChangedListener = func
    }

    var size: Float
        get() = controller.size
        set(value) {
            controller.size = value
        }

    fun setOnHistoryChangedListener(func: (List<DrawingOp>) -> Unit) {
        controller.onHistoryChangedListener = func
    }

    val history: List<DrawingOp>
        get() = controller.history

    fun undo() {
        if (controller.undo()) {
            invalidate()
        }
    }

    fun reset() {
        controller.reset()
        invalidate()
    }
}