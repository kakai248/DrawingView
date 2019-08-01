package com.kakai.android.drawingview.controls

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.kakai.android.drawingview.view.DrawingMode
import com.kakai.android.drawingview.view.DrawingOp
import com.kakai.android.drawingview.view.DrawingView
import kotlinx.android.synthetic.main.view_drawing_control.view.*
import top.defaults.colorpicker.ColorPickerPopup

class DrawingControlView : ConstraintLayout {

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

        onHistoryChanged(drawingView.history)
        drawingView.setOnHistoryChangedListener(::onHistoryChanged)
    }

    private fun onModeChanged(drawingMode: DrawingMode) {
        btnEraser.isChecked = drawingMode is DrawingMode.Eraser
        btnPath.isChecked = drawingMode is DrawingMode.Path
        btnRectangle.isChecked = drawingMode is DrawingMode.Rectangle
        btnOval.isChecked = drawingMode is DrawingMode.Oval
    }

    private fun onHistoryChanged(history: List<DrawingOp>) {
        btnUndo.isEnabled = history.isNotEmpty()
    }
}