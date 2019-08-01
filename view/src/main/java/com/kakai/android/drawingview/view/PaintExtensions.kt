package com.kakai.android.drawingview.view

import android.graphics.Paint

internal var Paint.drawingOptions: DrawingOp.Options
    get() = DrawingOp.Options(
        mode = xfermode,
        color = color,
        size = strokeWidth
    )
    set(value) {
        xfermode = value.mode
        color = value.color
        strokeWidth = value.size
    }