package com.andylai.interviewdemo.chronometer

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.andylai.interviewdemo.chronometer.DimenUtils.dpToPixel

class MaxHeightRecyclerView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

	override fun onMeasure(widthSpec: Int, heightSpec: Int) {
		val heightSpecMax = MeasureSpec.makeMeasureSpec(dpToPixel(120f), MeasureSpec.AT_MOST)
		super.onMeasure(widthSpec, heightSpecMax)
	}
}