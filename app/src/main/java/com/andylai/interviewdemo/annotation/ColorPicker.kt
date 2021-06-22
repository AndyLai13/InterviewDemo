package com.andylai.interviewdemo.annotation

import com.andylai.interviewdemo.R


@Suppress("MemberVisibilityCanBePrivate")
object ColorPicker {

	// ColorTicket is a voucher that users take it to exchange Color they want.
	data class ColorTicket(val colorType: ColorType, val index: Int)


	fun getColorRes(colorTicket: ColorTicket): Int {
		return if (colorTicket.colorType == ColorType.Pen) {
			RES_PEN_COLORS[colorTicket.index]
		} else {
			RES_HIGHLIGHTER_COLORS[colorTicket.index]
		}
	}

	val RES_PEN_COLORS = listOf(
		R.color.Picker1,
		R.color.Picker2,
		R.color.Picker3,
		R.color.Picker4,
		R.color.Picker5,
		R.color.Picker6,
		R.color.Picker7,
		R.color.Picker8,
	)

	val RES_HIGHLIGHTER_COLORS = listOf(
		R.color.Picker9,
		R.color.Picker10,
		R.color.Picker3,
		R.color.Picker4,
		R.color.Picker5,
		R.color.Picker6,
		R.color.Picker7,
		R.color.Picker8,
	)

	val RES_PEN_COLOR_DRAWABLES = listOf(
		R.drawable.sel_color_1,
		R.drawable.sel_color_2,
		R.drawable.sel_color_3,
		R.drawable.sel_color_4,
		R.drawable.sel_color_5,
		R.drawable.sel_color_6,
		R.drawable.sel_color_7,
		R.drawable.sel_color_8,
	)

	val RES_HIGHLIGHTER_COLORS_DRAWABLES = listOf(
		R.drawable.sel_color_9,
		R.drawable.sel_color_10,
		R.drawable.sel_color_3,
		R.drawable.sel_color_4,
		R.drawable.sel_color_5,
		R.drawable.sel_color_6,
		R.drawable.sel_color_7,
		R.drawable.sel_color_8,
	)

	val ColorSize = RES_PEN_COLORS.size

	enum class ColorType {
		Pen,
		HighLighter
	}
}