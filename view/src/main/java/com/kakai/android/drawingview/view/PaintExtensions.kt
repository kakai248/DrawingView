package com.kakai.android.drawingview.view

import android.graphics.Paint

internal var Paint.drawingOptions: DrawingOp.Options
    get() = DrawingOp.Options(
        color = color
    )
    set(value) {
        color = value.color
    }