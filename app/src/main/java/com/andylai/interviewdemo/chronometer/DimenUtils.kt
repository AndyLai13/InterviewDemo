package com.andylai.interviewdemo.chronometer

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

object DimenUtils {
    @JvmStatic
    fun dpToPixel(dp: Float): Int {
        return dpToPixelFloat(dp).toInt()
    }

    private fun dpToPixelFloat(dp: Float): Float {
        val dm = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, dm)
    }
}