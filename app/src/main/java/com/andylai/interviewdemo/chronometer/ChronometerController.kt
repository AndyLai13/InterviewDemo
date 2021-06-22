package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.viewsonic.sidetoolbar.chronometer.*
import com.viewsonic.sidetoolbar.chronometer.ButtonState.*
import com.andylai.interviewdemo.chronometer.Type.*

class ChronometerController(val context: Context) : TimerControllable, Movable {

	private val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
	private val mediumChronometerView: MediumChronometerView = MediumChronometerView(context)
	private val largeChronometerView: LargeChronometerView = LargeChronometerView(context)
	private val smallChronometerView: SmallChronometerView = SmallChronometerView(context)

	private val lapTimes = mutableListOf<String>()

	private var chronometerView: ChronometerView? = null
	private var buttonState: ButtonState = Start
	private var chronometer: Chronometer? = null
	private var isInit = false
	private var currentTime = 0L
	private var serviceCallback : ServiceCallback? = null
	init {
		serviceCallback = context as? ServiceCallback
		chronometer = object : Chronometer() {
			override fun onUpdate(millis: Long) = updateTime(millis)
			override fun onLap(millis: Long) = updateLapTimes(millis)
		}.also {
			it.onUpdate(0)
		}
	}

	fun showView(){
		if (isInit) {
			Toast.makeText(context, "Chronometer is already activated.", Toast.LENGTH_SHORT).show()
		} else {
			changeType()
			isInit = true
		}
	}

	private fun deinit() {
		chronometerView?.let { windowManager.removeView(it) }
		reset()
		buttonState = Start
		chronometerView = null
		isInit = false
		serviceCallback?.onRelease()
	}

	override fun onStart() = start()
	override fun onPause() = pause()
	override fun onLap() = lap()
	override fun onResume() = resume()
	override fun onReset() = reset()
	override fun onChangeType(type: Type) = changeType(type)
	override fun onUpdateState(buttonState: ButtonState) = updateState(buttonState)
	override fun onDeinit() = deinit()

	override fun onUpdateViewLayout(view: View, params: ViewGroup.LayoutParams) {
		updateViewLayout(view, params)
	}

	private fun start() {
		chronometer?.start()
	}

	private fun pause() {
		chronometer?.pause()
	}

	private fun lap() {
		chronometer?.lap()
	}

	private fun resume() {
		chronometer?.resume()
	}

	private fun reset() {
		chronometer?.reset()
		lapTimes.clear()
		chronometerView?.updateLapTimes(lapTimes)
	}

	private fun updateState(buttonState: ButtonState) {
		this.buttonState = buttonState
	}

	fun changeType(type: Type = Medium) {
		chronometerView?.let { windowManager.removeView(it) }

		chronometerView = when (type) {
			Large -> largeChronometerView
			Medium -> mediumChronometerView
			Small -> smallChronometerView
		}.also {
			windowManager.addView(it, it.getDefaultLayoutParams())
		}.also {
			it.bind(this)
		}
		val time = TimeFormatter.format(millis = currentTime, showTenMillis = true)
		chronometerView?.updateTime(time)
		chronometerView?.updateLapTimes(lapTimes)
		(chronometerView as? InteractiveChronometerView)?.changeButtonState(buttonState)
	}

	private fun updateTime(millis: Long) {
		currentTime = millis
		val time = TimeFormatter.format(millis = millis, showTenMillis = true)
		chronometerView?.updateTime(time)
	}

	private fun updateLapTimes(lapTime: Long) {
		lapTimes.add(TimeFormatter.format(lapTime))
		chronometerView?.updateLapTimes(lapTimes)
	}

	private fun updateViewLayout(view: View, params: ViewGroup.LayoutParams) {
		windowManager.updateViewLayout(view, params)
	}
}