package com.kakai.android.drawingview.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewDrawingControl.onUndoClick {
            viewDrawing.undo()
        }

        viewDrawingControl.onClearClick {
            viewDrawing.reset()
        }

        viewDrawingControl.onColorClick {
            ColorPickerDialog.Builder(this)
                .setPositiveButton("Select",
                    object : ColorEnvelopeListener {
                        override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                            viewDrawing.color = envelope.color
                        }
                    })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}