package com.example.paint

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintArea(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = 50f // Установите размер шрифта
    }

    private val path = Path()
    private val points = mutableListOf<Pair<Float, Float>>()
    private val texts = mutableListOf<Pair<String, Pair<Float, Float>>>()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
        for (point in points) {
            canvas.drawCircle(point.first, point.second, 5f, paint)
        }
        for (text in texts) {
            canvas.drawText(text.first, text.second.first, text.second.second, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isWriting) {
                    texts.add(Pair(currentText, Pair(event.x, event.y)))
                    invalidate()
                } else {
                    path.moveTo(event.x, event.y)
                    points.add(Pair(event.x, event.y))
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isWriting) {
                    path.lineTo(event.x, event.y)
                    points.add(Pair(event.x, event.y))
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isWriting) {
                    path.lineTo(event.x, event.y)
                    invalidate()
                }
            }
        }
        return true
    }

    fun clear() {
        path.reset()
        points.clear()
        texts.clear()
        invalidate()
    }

    var currentText: String = ""
    var isWriting: Boolean = false
}
