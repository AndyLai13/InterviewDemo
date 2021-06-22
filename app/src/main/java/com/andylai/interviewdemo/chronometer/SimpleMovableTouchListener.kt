package com.andylai.interviewdemo.chronometer

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlin.math.hypot

class SimpleMovableTouchListener(val params: WindowManager.LayoutParams,
                                 private val positionCallback: PositionCallback
) : View.OnTouchListener {

	private var viewLocation: PointF = PointF(0f, 0f)
	private var initialPoint: PointF = PointF(0f, 0f)
	private val tolerance = 10

	override fun onTouch(v: View, event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				viewLocation.set(params.x.toFloat(), params.y.toFloat())
				initialPoint.set(event.rawX, event.rawY)
				return true
			}
			MotionEvent.ACTION_MOVE -> {
				val updateX = viewLocation.x + (event.rawX - initialPoint.x)
				val updateY = viewLocation.y + (event.rawY - initialPoint.y)
				positionCallback.onUpdatePosition(updateX, updateY)
				return true
			}
			MotionEvent.ACTION_UP -> {
				val touchPoint = PointF(event.rawX, event.rawY)
				val diff = distance(touchPoint, initialPoint)
				if (diff < tolerance) {
					v.performClick()
				}
				return true
			}
		}
		return false
	}

	private fun distance(p1: PointF, p2: PointF) = hypot((p1.x - p2.x), (p1.y - p2.y))
}