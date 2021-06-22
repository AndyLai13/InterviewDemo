package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andylai.interviewdemo.R
import com.andylai.interviewdemo.chronometer.InteractiveChronometerView.LapButtonState.*
import com.viewsonic.sidetoolbar.chronometer.ButtonState

abstract class InteractiveChronometerView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChronometerView(context, attrs, defStyleAttr) {

	abstract val btnStart: Button
	abstract val btnPause: Button
	abstract val btnContinueAndReset: View
	abstract val btnContinue: Button
	abstract val btnReset: Button
	abstract val btnLap: Button
	abstract val recyclerView: RecyclerView
	abstract val lapContainer: View

	private val lapTimes = mutableListOf<String>()
	private val lapViewAdapter = LapViewAdapter(lapTimes)

	override fun bind(controller: ChronometerController) {
		super.bind(controller)

		btnStart.setOnClickListener {
			changeButtonState(ButtonState.Pause)
			timerControllable.onStart()
		}

		btnPause.setOnClickListener {
			changeButtonState(ButtonState.ContinueAndReset)
			timerControllable.onPause()
		}

		btnContinue.setOnClickListener {
			changeButtonState(ButtonState.Pause)
			timerControllable.onResume()
		}

		btnReset.setOnClickListener {
			changeButtonState(ButtonState.Start)
			timerControllable.onReset()
		}

		btnLap.setOnClickListener {
			timerControllable.onLap()
			if (lapTimes.size >= 10) {
				changeLapClickState(UnclickableMaxLap)
			}
		}

		recyclerView.apply {
			adapter = lapViewAdapter
			layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
			addItemDecoration(object : RecyclerView.ItemDecoration() {
				override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
					super.onDrawOver(c, parent, state)
					showLastData(parent)
				}

				private fun showLastData(recyclerView: RecyclerView) {
					val size: Int? = recyclerView.adapter?.itemCount
					size?.let { recyclerView.scrollToPosition(size - 1) }
				}

				override fun getItemOffsets(outRect: Rect,
				                            view: View,
				                            parent: RecyclerView,
				                            state: RecyclerView.State) {
					val space = 10
					outRect.left = space
					outRect.bottom = space
					outRect.right = space
					outRect.top = space
				}
			})
		}
	}

	fun changeButtonState(buttonState: ButtonState) {
		when (buttonState) {
			ButtonState.Start -> {
				btnStart.visibility = VISIBLE
				btnPause.visibility = GONE
				btnContinueAndReset.visibility = GONE
				changeLapClickState(Invisible)
				lapContainer.visibility = GONE
			}
			ButtonState.Pause -> {
				btnStart.visibility = GONE
				btnPause.visibility = VISIBLE
				btnContinueAndReset.visibility = GONE
				changeLapClickState(Clickable)
				lapContainer.visibility = VISIBLE
			}
			ButtonState.ContinueAndReset -> {
				btnStart.visibility = GONE
				btnPause.visibility = GONE
				btnContinueAndReset.visibility = VISIBLE
				changeLapClickState(Unclickable)
				lapContainer.visibility = VISIBLE
			}
		}
		timerControllable.onUpdateState(buttonState)
	}

	enum class LapButtonState {
		Invisible,
		Clickable,
		Unclickable,
		UnclickableMaxLap
	}

	private fun changeLapClickState(state: LapButtonState) {
		btnLap.apply {
			when (state) {
				Invisible -> visibility = GONE
				Clickable -> {
					visibility = VISIBLE
					isClickable = true
					setText(R.string.lap)
					setTextColor(Color.WHITE)
					setBackgroundResource(R.drawable.shape_rectangle_curved_green)
				}
				Unclickable -> {
					visibility = VISIBLE
					isClickable = false
					setText(R.string.lap)
					setTextColor(Color.DKGRAY)
					setBackgroundResource(R.drawable.shape_rectangle_curved_grey)
				}
				UnclickableMaxLap -> {
					visibility = VISIBLE
					isClickable = false
					setText(R.string.maximum_lap)
					setTextColor(Color.DKGRAY)
					setBackgroundResource(R.drawable.shape_rectangle_curved_grey)
				}
			}
		}
	}

	// test only
	@Suppress("unused")
	fun updateTime() {
		val millis1 = TimeFormatter.buildTime(hour = 1L, minute = 26L, second = 5L)
		val millis2 = TimeFormatter.buildTime(minute = 26L, second = 6L, tenMillis = 300L)
		val time1 = TimeFormatter.format(millis1)
		val time2 = TimeFormatter.format(millis2)
		Log.d("Andy", "times 1 =${time1}")
		Log.d("Andy", "times  2=${time2}")
		timeView.text = time2
	}

	override fun updateLapTimes(lapTimes: MutableList<String>) {
		this.lapTimes.clear()
		this.lapTimes.addAll(lapTimes)
		lapViewAdapter.notifyDataSetChanged()
	}
}