package com.andylai.interviewdemo.bounce

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.andylai.interviewdemo.R

class BounceView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
	// moving distance from current x/y
	private var dx = 2f
	private var dy = 5f
	// current position of object
	private var xCurrent = 0f
	private var yCurrent = 0f

	private var xLeft = 0
	private var yTop = 0
	private var xRight = 0
	private var yBottom = 0

	private var bmp: Bitmap
	private val imageResId = R.drawable.ic_logo_synology
	private val imageDrawable = ResourcesCompat.getDrawable(resources, imageResId, null)
	private val imageDrawableWidth:Int
	private val imageDrawableHeight:Int

	init {
		require(imageDrawable != null) {"Must add a logo."}
		bmp = BitmapFactory.decodeResource(resources, imageResId)
		imageDrawableWidth = imageDrawable.minimumWidth
		imageDrawableHeight = imageDrawable.minimumHeight
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		super.onLayout(changed, left, top, right, bottom)
		xLeft = left
		yTop = top
		xRight = right - imageDrawableWidth
		yBottom = bottom - imageDrawableHeight
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		if (dx != 0f || dy != 0f) {
			updateCurrentXY()
			canvas.drawBitmap(bmp, xCurrent, yCurrent, null)
			invalidate()
		}
	}

	private fun updateCurrentXY() {
		if ((dy > 0 && yCurrent - yBottom >= 0) || (dy < 0 && yCurrent - yTop <= 0))
			dy = -dy
		if ((dx > 0 && xCurrent - xRight >= 0) || (dx < 0 && xCurrent - xLeft <= 0))
			dx = -dx
		xCurrent += dx
		yCurrent += dy
	}
}