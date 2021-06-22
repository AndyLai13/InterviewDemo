package com.andylai.interviewdemo.chronometer


interface TimerView {
	fun bind(controller: ChronometerController)
	fun updateTime(time: String)
	fun updateLapTimes(lapTimes: MutableList<String>)
}