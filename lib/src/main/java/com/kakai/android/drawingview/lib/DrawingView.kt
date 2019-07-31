package com.kakai.android.drawingview.lib

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

    override fun onDraw(canvas: Canvas) {
        controller.render(canvas)
    }

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

    var color: Int
        get() = controller.color
        set(value) {
            controller.color = value
        }

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