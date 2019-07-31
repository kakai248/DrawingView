package com.kakai.android.drawingview.lib

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_drawing_control.view.*

class DrawingControlView : LinearLayout {

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
    }

    fun onUndoClick(func: () -> Unit) {
        btnUndo.setOnClickListener { func() }
    }

    fun onClearClick(func: () -> Unit) {
        btnClear.setOnClickListener { func() }
    }

    fun onColorClick(func: () -> Unit) {
        btnColor.setOnClickListener { func() }
    }
}