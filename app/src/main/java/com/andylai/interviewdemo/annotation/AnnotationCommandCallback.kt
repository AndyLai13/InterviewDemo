package com.andylai.interviewdemo.annotation

interface AnnotationCommandCallback {
	fun onSetPenMode()
	fun onSetHighlighterMode()
	fun onSetEraserMode()
	fun onRedo()
	fun onUndo()
	fun onDelete()
	fun onSave()
	fun onClose()
}