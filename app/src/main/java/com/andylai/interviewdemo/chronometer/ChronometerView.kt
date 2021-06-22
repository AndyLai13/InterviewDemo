package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.util.AttributeSet
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView

abstract class ChronometerView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TimerView {

	abstract val timeView: TextView
	lateinit var timerControllable: TimerControllable
	lateinit var movable: Movable

	override fun bind(controller: ChronometerController) {
		timerControllable = controller
		movable = controller
	}

	override fun updateTime(time: String) {
		timeView.text = time
	}

	fun changeType(type: Type) {
		timerControllable.onChangeType(type)
	}

	abstract override fun updateLapTimes(lapTimes: MutableList<String>)
	abstract fun getDefaultLayoutParams(): WindowManager.LayoutParams
}