package com.andylai.interviewdemo.annotation

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.andylai.interviewdemo.R

class AnnotationBasicButton @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
	init {
		isClickable = true
		val typedArray: TypedArray = context.obtainStyledAttributes(
			attrs, R.styleable.AnnotationBasicButton, defStyleAttr, 0
		)
		val icon = typedArray.getResourceId(R.styleable.AnnotationBasicButton_icon, R.drawable.ic_anno_close)
		setImageResource(icon)
		typedArray.recycle()
	}
}