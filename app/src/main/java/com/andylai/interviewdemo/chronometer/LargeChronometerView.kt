package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andylai.interviewdemo.R

class LargeChronometerView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InteractiveChronometerView(context, attrs, defStyleAttr) {

	private val btnClose: ImageView

	init {
		LayoutInflater.from(context).inflate(R.layout.floating_chronometer_large, this)
		btnClose = findViewById(R.id.close)
		btnClose.setOnClickListener { changeType(Type.Medium) }
	}

	override val timeView: TextView =findViewById(R.id.timeView)
	override val btnStart: Button = findViewById(R.id.btnStart)
	override val btnPause: Button = findViewById(R.id.btnPause)
	override val btnContinueAndReset: View = findViewById(R.id.btnContinueAndReset)
	override val btnContinue: Button = findViewById(R.id.btnContinue)
	override val btnReset: Button = findViewById(R.id.btnReset)
	override val btnLap: Button = findViewById(R.id.lap)
	override val recyclerView: RecyclerView = findViewById(R.id.recycler)
	override val lapContainer: View = findViewById(R.id.lapContainer)

	override fun getDefaultLayoutParams(): WindowManager.LayoutParams {
		return Utilities.getFullScreenLayoutParams()
	}
}