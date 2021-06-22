package com.andylai.interviewdemo.chronometer

import android.view.View
import android.view.ViewGroup

interface Movable {
	fun onUpdateViewLayout(view: View, params: ViewGroup.LayoutParams)
}