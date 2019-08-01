package com.kakai.android.drawingview.view

sealed class DrawingMode {
    object Path : DrawingMode()
    object Rectangle : DrawingMode()
    object Oval : DrawingMode()
    object Triangle : DrawingMode()
    object Eraser : DrawingMode()
}