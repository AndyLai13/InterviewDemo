package com.andylai.interviewdemo.chronometer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager

object Utilities {

	@JvmStatic
	fun getFloatDensitySize(context: Context, x: Int): Int {
		val density = context.resources.displayMetrics.density
		val densityFactor = density / 1.5f
		return (x * densityFactor).toInt()
	}

	@JvmStatic
	@SuppressLint("InlinedApi")
	fun getDefaultLayoutParamsWithPosition(xpos: Int, ypos: Int): WindowManager.LayoutParams {
		return WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			xpos, ypos,
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT)
	}

	fun getFullScreenLayoutParams()  =
		WindowManager.LayoutParams(
			WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.MATCH_PARENT,
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT)

	@JvmStatic
	val defaultLayoutParams: WindowManager.LayoutParams
		get() = getDefaultLayoutParams(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)

	@JvmStatic
	@SuppressLint("InlinedApi")
	fun getDefaultLayoutParams(flag: Int): WindowManager.LayoutParams {
		return WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
			flag,
			PixelFormat.TRANSLUCENT)
	}
}