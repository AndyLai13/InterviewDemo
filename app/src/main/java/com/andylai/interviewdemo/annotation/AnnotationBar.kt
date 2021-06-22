package com.andylai.interviewdemo.annotation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.andylai.interviewdemo.databinding.AnnotationBarBinding

class AnnotationBar @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

	var binding: AnnotationBarBinding
	private val callback: Callback
	private var mode = Mode.PEN

	init {
		require(context is Callback) { "Must implement AnnotationBar.Callback" }
		callback = context
		binding = AnnotationBarBinding.inflate(LayoutInflater.from(context),this, true)

		// set initial state
		changeMode(Mode.PEN)

		binding.pen.setOnClickListener { handleClickOrChangeMode(Mode.PEN) }
		binding.highlighter.setOnClickListener { handleClickOrChangeMode(Mode.HIGHLIGHTER) }
		binding.eraser.setOnClickListener { handleClickOrChangeMode(Mode.ERASER) }
		binding.undo.setOnClickListener { callback.onUndoClick() }
		binding.redo.setOnClickListener { callback.onRedoClick() }
		binding.delete.setOnClickListener { callback.onDeleteClick() }
		binding.save.setOnClickListener { callback.onSaveClick() }
		binding.close.setOnClickListener { callback.onCloseClick() }
	}

	private fun handleClickOrChangeMode(targetMode: Mode) {
		if (mode == targetMode) {
			callback.onClickUnderPressedState(mode)
		} else {
			callback.onChangeMode(targetMode)
			changeMode(targetMode)
		}
	}

	fun changeMode(mode: Mode) {
		this.mode = mode
		when (mode) {
			Mode.PEN -> {
				binding.pen.isPressed = true
				binding.highlighter.isPressed = false
				binding.eraser.isPressed = false
			}
			Mode.HIGHLIGHTER -> {
				binding.pen.isPressed = false
				binding.highlighter.isPressed = true
				binding.eraser.isPressed = false
			}
			Mode.ERASER -> {
				binding.pen.isPressed = false
				binding.highlighter.isPressed = false
				binding.eraser.isPressed = true
			}
		}
	}

	fun setPenColor(colorInt: Int) {
		Log.d("Andy", "annotation setPenColor color = $colorInt")

		binding.pen.setColor(colorInt)
	}

	fun setHighlighterColor(color: Int) {
		binding.highlighter.setColor(color)
	}

	fun setRedoEnabled(isEnabled: Boolean) {
		binding.redo.isEnabled = isEnabled
	}

	fun setUndoEnabled(isEnabled: Boolean) {
		binding.undo.isEnabled = isEnabled
	}

	interface Callback {
		fun onClickUnderPressedState(mode: Mode)
		fun onChangeMode(mode: Mode)
		fun onUndoClick()
		fun onRedoClick()
		fun onDeleteClick()
		fun onSaveClick()
		fun onCloseClick()
	}

	enum class Mode {
		PEN, HIGHLIGHTER, ERASER
	}
}