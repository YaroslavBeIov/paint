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
        setLayerType(LAYER_TYPE_SOFTWARE, null) // Оптимизация для рисования
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint) // Рисуем путь
        for (point in points) {
            canvas.drawCircle(point.first, point.second, 5f, paint) // Рисуем точки
        }
        for (text in texts) {
            canvas.drawText(text.first, text.second.first, text.second.second, textPaint) // Рисуем текст
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isWriting) { // Проверка, что мы пишем
                    // Сохраняем текст по координатам
                    texts.add(Pair(currentText, Pair(event.x, event.y)))
                    invalidate() // Перерисовка
                } else {
                    path.moveTo(event.x, event.y)
                    points.add(Pair(event.x, event.y))
                    invalidate() // Перерисовка
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isWriting) {
                    path.lineTo(event.x, event.y)
                    points.add(Pair(event.x, event.y))
                    invalidate() // Перерисовка
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isWriting) {
                    path.lineTo(event.x, event.y)
                    invalidate() // Перерисовка
                }
            }
        }
        return true
    }

    fun clear() {
        path.reset() // Очищаем путь
        points.clear() // Очищаем точки
        texts.clear() // Очищаем текст
        invalidate() // Перерисовка
    }

    // Для работы с текстом
    var currentText: String = ""
    var isWriting: Boolean = false
}
