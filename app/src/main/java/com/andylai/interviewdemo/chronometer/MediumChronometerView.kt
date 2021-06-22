package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andylai.interviewdemo.R

class MediumChronometerView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InteractiveChronometerView(context, attrs, defStyleAttr) {

	init {
		LayoutInflater.from(context).inflate(R.layout.floating_chronometer, this)
		findViewById<View>(R.id.expand).setOnClickListener { changeType(Type.Large) }
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_OUTSIDE) {
			changeType(Type.Small)
			return true
		}
		return super.onTouchEvent(event)
	}

	override val timeView: TextView = findViewById(R.id.timeView)
	override val btnStart: Button = findViewById(R.id.btnStart)
	override val btnPause: Button = findViewById(R.id.btnPause)
	override val btnContinueAndReset: View = findViewById(R.id.btnContinueAndReset)
	override val btnContinue: Button = findViewById(R.id.btnContinue)
	override val btnReset: Button = findViewById(R.id.btnReset)
	override val btnLap: Button = findViewById(R.id.lap)
	override val recyclerView: RecyclerView = findViewById(R.id.recycler)
	override val lapContainer: View = findViewById(R.id.lapContainer)

	override fun bind(controller: ChronometerController) {
		super.bind(controller)
		val params = getDefaultLayoutParams()

		findViewById<View>(R.id.close).setOnClickListener { timerControllable.onDeinit() }
		findViewById<View>(R.id.root).setOnTouchListener(
			SimpleMovableTouchListener(
				params, object : PositionCallback {
			override fun onUpdatePosition(x: Float, y: Float) {
				params.x = x.toInt()
				params.y = y.toInt()
				movable.onUpdateViewLayout(this@MediumChronometerView, params)
			}
		})
		)
	}

	override fun getDefaultLayoutParams(): WindowManager.LayoutParams {
		return Utilities.defaultLayoutParams
	}
}