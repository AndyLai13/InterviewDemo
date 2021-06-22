package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.andylai.interviewdemo.R
import com.andylai.interviewdemo.chronometer.Utilities.getDefaultLayoutParamsWithPosition
import com.andylai.interviewdemo.chronometer.Utilities.getFloatDensitySize

class SmallChronometerView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChronometerView(context, attrs, defStyleAttr) {

	init {
		LayoutInflater.from(context).inflate(R.layout.floating_chronometer_small, this)
	}

	override val timeView: TextView = findViewById(R.id.timeView)

	override fun bind(controller: ChronometerController) {
		super.bind(controller)
		val params = getDefaultLayoutParams()

		timeView.setOnClickListener { changeType(Type.Medium) }
		timeView.setOnTouchListener(
			SimpleMovableTouchListener(
				params, object : PositionCallback {
			override fun onUpdatePosition(x: Float, y: Float) {
				params.x = x.toInt()
				params.y = y.toInt()
				movable.onUpdateViewLayout(this@SmallChronometerView, params)
			}
		})
		)
	}

	override fun updateTime(time: String) {
		timeView.text = time
	}

	override fun updateLapTimes(lapTimes: MutableList<String>) {
	}

	override fun getDefaultLayoutParams(): WindowManager.LayoutParams {
		return getDefaultLayoutParamsWithPosition(
			getFloatDensitySize(context, 200),
			getFloatDensitySize(context, 500)
		)
	}
}