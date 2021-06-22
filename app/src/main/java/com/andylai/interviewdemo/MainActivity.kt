package com.andylai.interviewdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andylai.interviewdemo.annotation.AnnotationActivity
import com.andylai.interviewdemo.bounce.BounceActivity
import com.andylai.interviewdemo.chronometer.ChronometerActivity
import com.andylai.interviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		binding.bounceLogo.setOnClickListener {
			startActivity(Intent(this, BounceActivity::class.java))
		}
		binding.annotation.setOnClickListener {
			startActivity(Intent(this, AnnotationActivity::class.java))
		}
		binding.chronometer.setOnClickListener {
			startActivity(Intent(this, ChronometerActivity::class.java))
		}
	}
}