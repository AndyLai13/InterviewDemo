package com.andylai.interviewdemo.chronometer

import android.os.*

abstract class Chronometer {

	companion object {
		const val MSG = 1
	}

	private var startTime = 0L
	private var passedTime = 0L
	private var lapTime = 0L
	private var lapStartTime = 0L
	private var pausedTime = 0L
	private var paused = false
	private var laped = false

	@Synchronized
	fun start() {
		startTime = SystemClock.elapsedRealtime()
		lapStartTime = SystemClock.elapsedRealtime()
		handler.sendMessage(handler.obtainMessage(MSG))
	}

	@Synchronized
	fun pause() {
		paused = true
		pausedTime = SystemClock.elapsedRealtime()
	}

	@Synchronized
	fun resume() {
		paused = false
		startTime += (SystemClock.elapsedRealtime() - pausedTime)
		handler.sendMessage(handler.obtainMessage(MSG))
	}

	@Synchronized
	fun reset() {
		paused = false
		onUpdate(0)
		handler.removeMessages((MSG))
	}

	@Synchronized
	fun lap() {
		laped = true
	}

	private val handler = object : Handler(Looper.getMainLooper()) {
		override fun handleMessage(msg: Message) {
			synchronized(this@Chronometer) {
				if (paused) return
				passedTime = SystemClock.elapsedRealtime() - startTime
				lapTime = SystemClock.elapsedRealtime() - lapStartTime
				onUpdate(passedTime)
				if (laped) {
					laped = false
					onLap(lapTime)
					lapStartTime = SystemClock.elapsedRealtime()
				}
				sendMessageDelayed(obtainMessage(MSG), 10)
			}
		}
	}

	abstract fun onUpdate(millis: Long)
	abstract fun onLap(millis: Long)
}