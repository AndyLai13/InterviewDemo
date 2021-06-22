package com.andylai.interviewdemo.annotation

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.andylai.interviewdemo.annotation.AnnotationBar.Mode
import com.andylai.interviewdemo.annotation.ColorPicker.ColorTicket
import com.andylai.interviewdemo.databinding.ActivityAnnotationBinding
import kotlin.math.hypot

class AnnotationActivity : AppCompatActivity(), AnnotationBar.Callback,
	ColorPickerRadioGroup.OnChangeColorListener {


	private lateinit var binding: ActivityAnnotationBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAnnotationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setDragEvent()
		setDefaultColor()
		Handler(Looper.getMainLooper()).postDelayed({
		}, 500)
	}

	@SuppressLint("ClickableViewAccessibility")
	private fun setDragEvent() {
		binding.annotationBar.setOnTouchListener(
			object : View.OnTouchListener {
				private var viewLocation: PointF = PointF(0f, 0f)
				private var initialPoint: PointF = PointF(0f, 0f)

				override fun onTouch(v: View, event: MotionEvent): Boolean {
					when (event.action) {
						MotionEvent.ACTION_DOWN -> {
							viewLocation.set(
								binding.compositeBar.x.toFloat(),
								binding.compositeBar.y.toFloat()
							)
							initialPoint.set(event.rawX, event.rawY)
							return true
						}
						MotionEvent.ACTION_MOVE -> {
							val updateX = viewLocation.x + (event.rawX - initialPoint.x)
							val updateY = viewLocation.y + (event.rawY - initialPoint.y)
							binding.compositeBar.x = updateX
							binding.compositeBar.y = updateY
							binding.compositeBar.invalidate()
							return true
						}
						MotionEvent.ACTION_UP -> {
							return true
						}
					}
					return false
				}

				private fun distance(p1: PointF, p2: PointF) = hypot((p1.x - p2.x), (p1.y - p2.y))
			})
	}

	private fun setDefaultColor() {
		setPenColor(Color.RED)
		setHighlighterColor(Color.YELLOW)
	}

	private fun setPenColor(color: Int) {
		binding.annotationBar.setPenColor(color)
		binding.annotationCanvas.setPenColor(color)
	}

	private fun setHighlighterColor(color: Int) {
		binding.annotationBar.setHighlighterColor(color)
		binding.annotationCanvas.setHighlightColor(color)
	}

	private fun setPenMode() {
		binding.annotationCanvas.setPenMode()
	}

	private fun setHighlighterMode() {
		binding.annotationCanvas.setHighlighterMode()
	}

	private fun setEraserMode() {
		binding.annotationCanvas.setEraserMode()
	}

	override fun onUndoClick() = logAndInvoke({ undo() })
	override fun onRedoClick() = logAndInvoke({ redo() })
	override fun onDeleteClick() = logAndInvoke({ clear() })
	override fun onSaveClick() = logAndInvoke({ save() })
	override fun onCloseClick() = logAndInvoke({ close() })
	override fun onChangeColor(ticket: ColorTicket) = logAndInvoke({ changeColor(ticket) })
	override fun onChangeMode(mode: Mode) = logAndInvoke({ changeMode(mode) })
	override fun onClickUnderPressedState(mode: Mode) = logAndInvoke({ setColorPickerVisibility() })

	private fun undo() = logAndInvoke({ binding.annotationCanvas.undo() })
	private fun redo() = logAndInvoke({ binding.annotationCanvas.redo() })

	private fun clear() {
		binding.annotationCanvas.clear()
		//		AcceleratorManager.getInstance().setBackground(annotationCanvas);
	}

	private fun save() {

	}

	private fun close() = logAndInvoke({ finish() })

	override fun onStart() {
		super.onStart()
		//		AcceleratorManager.getInstance().onStart();
	}

	override fun onStop() {
		super.onStop()
		//		AcceleratorManager.getInstance().onStop();
	}

	override fun onDestroy() {
		super.onDestroy()
		//		AcceleratorManager.getInstance().onDestroy();
	}

	private fun setColorPickerVisibility() {
		val fromState = binding.colorPickerRadioGroup.visibility
		val toState = if (fromState == View.VISIBLE) View.GONE else View.VISIBLE
		binding.colorPickerRadioGroup.visibility = toState
	}

	private fun changeMode(mode: Mode) {
		when (mode) {
			Mode.PEN -> {
				setPenMode()
				binding.colorPickerRadioGroup.changeColorType(ColorPicker.ColorType.Pen)
			}
			Mode.HIGHLIGHTER -> {
				setHighlighterMode()
				binding.colorPickerRadioGroup.changeColorType(ColorPicker.ColorType.HighLighter)
			}
			Mode.ERASER -> setEraserMode()
		}
	}

	private fun changeColor(ticket: ColorTicket) {
		val color = ColorPicker.getColorRes(ticket).toColor()
		if (ticket.colorType == ColorPicker.ColorType.Pen) {
			setPenColor(color)
		} else {
			setHighlighterColor(color)
		}
	}

	private fun Int.toColor(): Int = ResourcesCompat.getColor(resources, this, null)

	private fun logAndInvoke(vararg methods: () -> Unit) {
		methods.forEach { method ->
			val enclosingMethod = method.javaClass.enclosingMethod
			enclosingMethod?.let {
				Log.d("Andy", "invoke function -> ${enclosingMethod.name}")
				method()
			} ?: throw NullPointerException("Method should not be null, please check this.")
		}
	}
}