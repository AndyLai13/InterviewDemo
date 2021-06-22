package com.andylai.interviewdemo.annotation


import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import com.andylai.interviewdemo.R
import com.andylai.interviewdemo.annotation.ColorPicker.ColorSize
import com.andylai.interviewdemo.annotation.ColorPicker.ColorTicket
import com.andylai.interviewdemo.annotation.ColorPicker.ColorType
import com.andylai.interviewdemo.annotation.ColorPicker.RES_HIGHLIGHTER_COLORS_DRAWABLES
import com.andylai.interviewdemo.annotation.ColorPicker.RES_PEN_COLOR_DRAWABLES

class ColorPickerRadioGroup(context: Context, attrs: AttributeSet? = null) :
	RadioGroup(context, attrs) {

	private val radioButtons = List(ColorSize) { AppCompatRadioButton(context) }
	private val onChangeColorListener: OnChangeColorListener
	private var resColorDrawables = listOf<Int>()
	private var colorType = ColorType.Pen

	init {
		require(context is OnChangeColorListener) { "Must implement OnChangeColorListener" }
		onChangeColorListener = context
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.ColorPickerRadioGroup, 0, 0
		)
		val colorTypeInt =
			typedArray.getInt(R.styleable.ColorPickerRadioGroup_colorType, ColorType.Pen.ordinal)
		val colorType =
			if (colorTypeInt == ColorType.Pen.ordinal) ColorType.Pen else ColorType.HighLighter
		typedArray.recycle()

		setRadioButtons(colorType)
		addButtonToView()
	}

	private fun addButtonToView() {
		val params = LayoutParams(
			resources.getDimension(R.dimen.color_picker).toInt(),
			resources.getDimension(R.dimen.color_picker).toInt()
		)
		radioButtons.forEach { addView(it, params) }
	}

	private fun setRadioButtons(colorType: ColorType) {
		changeColorType(colorType)
		setRadioButtonProps()
	}

	fun changeColorType(colorType: ColorType) {
		this.colorType = colorType
		setResColorDrawable(colorType)
		setRadioButtonBackground()
	}

	private fun setResColorDrawable(colorType: ColorType) {
		resColorDrawables =
			if (colorType == ColorType.Pen) RES_PEN_COLOR_DRAWABLES
			else RES_HIGHLIGHTER_COLORS_DRAWABLES
	}

	private fun setRadioButtonProps() {
		for (index in radioButtons.indices) {
			radioButtons[index].buttonDrawable = null
			radioButtons[index].setOnClickListener {
				onChangeColorListener.onChangeColor(
					ColorTicket(colorType, index)
				)
			}
		}
	}

	private fun setRadioButtonBackground() {
		for (index in radioButtons.indices) {
			radioButtons[index].background = ResourcesCompat.getDrawable(
				resources, resColorDrawables[index], null
			)
		}
	}

	interface OnChangeColorListener {
		fun onChangeColor(ticket: ColorTicket)
	}
}