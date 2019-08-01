package com.kakai.android.drawingview.controls

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.kakai.android.drawingview.view.DrawingMode
import com.kakai.android.drawingview.view.DrawingView
import kotlinx.android.synthetic.main.view_drawing_control.view.*
import top.defaults.colorpicker.ColorPickerPopup

class DrawingControlView : LinearLayout {

    private var drawingView: DrawingView? = null

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
        gravity = Gravity.CENTER
        View.inflate(context, R.layout.view_drawing_control, this)

        btnUndo.setOnClickListener {
            drawingView?.undo()
        }

        btnClear.setOnClickListener {
            drawingView?.reset()
        }

        btnEraser.setOnClickListener {
            drawingView?.mode = DrawingMode.Eraser
        }

        btnColor.setOnClickListener {
            val drawingView = drawingView ?: return@setOnClickListener

            ColorPickerPopup.Builder(context)
                .initialColor(drawingView.color)
                .enableBrightness(true)
                .enableAlpha(true)
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(object : ColorPickerPopup.ColorPickerObserver() {
                    override fun onColorPicked(color: Int) {
                        drawingView.color = color
                    }
                })
        }

        btnPath.setOnClickListener {
            drawingView?.mode = DrawingMode.Path
        }

        btnRectangle.setOnClickListener {
            drawingView?.mode = DrawingMode.Rectangle
        }

        btnOval.setOnClickListener {
            drawingView?.mode = DrawingMode.Oval
        }
    }

    fun attach(drawingView: DrawingView) {
        this.drawingView = drawingView

        onModeChanged(drawingView.mode)
        drawingView.setOnModeChangedListener(::onModeChanged)
    }

    private fun onModeChanged(drawingMode: DrawingMode) {
        btnEraser.isSelected = drawingMode is DrawingMode.Eraser
        btnPath.isSelected = drawingMode is DrawingMode.Path
        btnRectangle.isSelected = drawingMode is DrawingMode.Rectangle
        btnOval.isSelected = drawingMode is DrawingMode.Oval
    }
}