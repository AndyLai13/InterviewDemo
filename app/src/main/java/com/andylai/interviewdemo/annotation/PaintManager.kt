package com.andylai.interviewdemo.annotation

import android.graphics.*

object PaintManager {

	private val XFERMODE_DRAW: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
	private val XFERMODE_CLEAR: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

	@JvmStatic
	val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

	init {
		paint.strokeJoin = Paint.Join.ROUND
		paint.strokeCap = Paint.Cap.ROUND
	}

	@JvmStatic
	var drawMode = DrawMode.DRAW
		set(value) {
			field = value
			updateStrokePaint()
		}

	@JvmStatic
	var restoreDrawMode = drawMode

	@JvmStatic
	var penColor = Color.BLACK
		set(value) {
			field = value
			updateStrokePaint()
		}

	@JvmStatic
	var highlightColor = Color.YELLOW
		set(value) {
			field = value
			updateStrokePaint()
		}

	@JvmStatic
	fun restoreStrokePaint() {
		drawMode = restoreDrawMode
	}

	private fun updateStrokePaint() {
		when (drawMode) {
			DrawMode.DRAW -> {
				paint.color = penColor
				paint.strokeWidth = 10f
				paint.xfermode = XFERMODE_DRAW
				paint.style = Paint.Style.STROKE
				paint.alpha = 255
			}
			DrawMode.HIGHLIGHTER -> {
				paint.color = highlightColor
				paint.strokeWidth = 20f
				paint.xfermode = XFERMODE_DRAW
				paint.style = Paint.Style.STROKE
				paint.alpha = 128
			}
			DrawMode.ERASER -> {
				paint.xfermode = XFERMODE_CLEAR
				paint.strokeWidth = 20f
				paint.style = Paint.Style.STROKE
				paint.alpha = 255
			}
			DrawMode.PALM_ERASER -> {
				paint.xfermode = XFERMODE_CLEAR
				paint.strokeWidth = 1f
				paint.style = Paint.Style.FILL
				paint.alpha = 255
			}
		}
	}
}