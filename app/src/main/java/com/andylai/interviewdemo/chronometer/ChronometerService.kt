package com.andylai.interviewdemo.chronometer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ChronometerService : Service(),ServiceCallback {
	var chronometerController: ChronometerController? = null

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		startFloat()
		return super.onStartCommand(intent, flags, startId)
	}

	override fun onBind(intent: Intent): IBinder {
		return Binder()
	}

	private fun startFloat() {
		chronometerController = ChronometerController(this)
		chronometerController?.showView()
	}

	override fun onRelease() {
		chronometerController = null
		stopSelf()
	}
}