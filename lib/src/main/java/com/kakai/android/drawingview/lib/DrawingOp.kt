package com.kakai.android.drawingview.lib

import androidx.annotation.ColorInt

typealias AndroidPath = android.graphics.Path

sealed class DrawingOp(open val options: Options) {
    data class Path(
        override val options: Options,
        val path: AndroidPath
    ) : DrawingOp(options)

    data class Options(
        @ColorInt val color: Int
    )
}