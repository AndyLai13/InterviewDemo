package com.andylai.interviewdemo.chronometer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChronometerActivity : AppCompatActivity() {

	companion object {
		const val REQUEST_CODE_OVERLAY = 10
	}

	lateinit var chronometerController: ChronometerController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		checkDrawPermission()
	}

	private fun checkDrawPermission() {
		if (Settings.canDrawOverlays(this)) {
			Toast.makeText(this, "Already grant permission", Toast.LENGTH_SHORT).show()
			startChronometer()
		} else {
			val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
			intent.data = Uri.parse("package:$packageName")
			startActivityForResult(intent, REQUEST_CODE_OVERLAY)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		Handler(Looper.getMainLooper()).postDelayed({
			if (requestCode == REQUEST_CODE_OVERLAY) {
				if (Settings.canDrawOverlays(this)) {
					Toast.makeText(this, "Already grant permission", Toast.LENGTH_SHORT).show()
					startChronometer()
				} else {
					Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
					finish()
				}
			}
		}, 500)
	}

	private fun startChronometer() {
		startService(Intent(this, ChronometerService::class.java))
		finish()
	}
}

