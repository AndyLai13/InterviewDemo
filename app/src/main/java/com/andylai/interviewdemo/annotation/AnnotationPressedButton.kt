package com.andylai.interviewdemo.annotation

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.andylai.interviewdemo.R

class AnnotationPressedButton @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
	init {
		isClickable = true
		val typedArray: TypedArray = context.obtainStyledAttributes(
			attrs, R.styleable.AnnotationPressedButton, defStyleAttr, 0
		)
		val icon = typedArray.getResourceId(R.styleable.AnnotationPressedButton_icon, R.drawable.ic_anno_pen)
		setImageResource(icon)
		typedArray.recycle()
	}

	fun setColor(colorInt: Int) {
		setBackgroundColor(colorInt)
	}

	// Should override performClick to avoid lint warning
	override fun performClick(): Boolean {
		Log.d("AnnotationPressedButton", "Overriding performClick() to avoid lint warning.")
		return super.performClick()
	}

	override fun onTouchEvent(event: MotionEvent?): Boolean {
		return when(event?.action) {
			MotionEvent.ACTION_DOWN -> {
				isPressed = true
				true
			}
			MotionEvent.ACTION_UP -> {
				performClick()
				true
			}
			else -> super.onTouchEvent(event)
		}
	}
}