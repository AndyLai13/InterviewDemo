package com.viewsonic.sidetoolbar.chronometer

sealed class ButtonState {
	object Start : ButtonState()
	object Pause : ButtonState()
	object ContinueAndReset : ButtonState()
}