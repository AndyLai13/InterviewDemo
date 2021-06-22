package com.andylai.interviewdemo.chronometer

import com.viewsonic.sidetoolbar.chronometer.ButtonState

interface TimerControllable {
	fun onStart()
	fun onPause()
	fun onLap()
	fun onResume()
	fun onReset()
	fun onChangeType(type: Type)
	fun onUpdateState(buttonState: ButtonState)
	fun onDeinit()
}