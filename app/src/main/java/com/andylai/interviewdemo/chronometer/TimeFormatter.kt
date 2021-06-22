package com.andylai.interviewdemo.chronometer

@Suppress("MemberVisibilityCanBePrivate")
object TimeFormatter {
	const val MILLIS_PER_SECOND = 1000L
	const val SECOND_PER_MINUTE = 60L
	const val MINUTE_PER_HOUR = 60L
	const val MILLIS_PER_TEN_MILLIS = 10L
	private const val debug = false

	fun buildTime(hour: Long = 0, minute: Long = 0, second: Long = 0, tenMillis: Long = 0): Long {
		return hour * MINUTE_PER_HOUR * SECOND_PER_MINUTE * MILLIS_PER_SECOND +
				minute * SECOND_PER_MINUTE * MILLIS_PER_SECOND +
				second * MILLIS_PER_SECOND +
				tenMillis
	}

	fun format(millis: Long, showTenMillis: Boolean = false): String {
		var remainder = millis
		val hour = remainder / (MILLIS_PER_SECOND * SECOND_PER_MINUTE * MINUTE_PER_HOUR)
		remainder %= (MILLIS_PER_SECOND * SECOND_PER_MINUTE * MINUTE_PER_HOUR)
		val minute = remainder / (MILLIS_PER_SECOND * SECOND_PER_MINUTE)
		remainder %= (MILLIS_PER_SECOND * SECOND_PER_MINUTE)
		val second = (remainder / (MILLIS_PER_SECOND))
		remainder %= (MILLIS_PER_SECOND)
		val tenMillis = remainder / MILLIS_PER_TEN_MILLIS

		val formatHour = hour.addZeroForFirstEmptyDigit()
		val formatMinute = minute.addZeroForFirstEmptyDigit()
		val formatSecond = second.addZeroForFirstEmptyDigit()
		val formatTenMillis = tenMillis.addZeroForFirstEmptyDigit()

		val formatString: String = if (showTenMillis && hour <= 0) {
			"$formatMinute:$formatSecond.$formatTenMillis"
		} else {
			"$formatHour:$formatMinute:$formatSecond"
		}

		formatString.debugPrintln()
		return formatString
	}

	fun formatWithHour(millis: Long): String {
		var remainder = millis
		val hour = remainder / (MILLIS_PER_SECOND * SECOND_PER_MINUTE * MINUTE_PER_HOUR)
		remainder %= (MILLIS_PER_SECOND * SECOND_PER_MINUTE * MINUTE_PER_HOUR)
		val minute = remainder / (MILLIS_PER_SECOND * SECOND_PER_MINUTE)
		remainder %= (MILLIS_PER_SECOND * SECOND_PER_MINUTE)
		val second = (remainder / (MILLIS_PER_SECOND))
		remainder %= (MILLIS_PER_SECOND)
		val tenMillis = remainder / MILLIS_PER_TEN_MILLIS

		val formatHour = hour.addZeroForFirstEmptyDigit()
		val formatMinute = minute.addZeroForFirstEmptyDigit()
		val formatSecond = second.addZeroForFirstEmptyDigit()
		val formatTenMillis = tenMillis.addZeroForFirstEmptyDigit()

		val formatString ="$formatHour:$formatMinute:$formatSecond"
		formatString.debugPrintln()
		return formatString
	}

	fun formatWithMillis(hour: Int, minute: Int, second: Int): Long  {
		return (hour * MINUTE_PER_HOUR * SECOND_PER_MINUTE * MILLIS_PER_SECOND) + (minute * SECOND_PER_MINUTE * MILLIS_PER_SECOND) + (second * MILLIS_PER_SECOND)
	}

	private fun Long.addZeroForFirstEmptyDigit(): String {
		return String.format("%02d", this)
	}

	private fun String.debugPrintln() {
		if (debug) println(this)
	}
}

fun main(args: Array<String>) {
	val millis1 = TimeFormatter.buildTime(hour = 1L, minute = 26L, second = 5L)
	val millis2 = TimeFormatter.buildTime(minute = 26L, second = 6L, tenMillis = 300L)
	val time1 = TimeFormatter.format(millis1)
	val time2 = TimeFormatter.format(millis2)
	println("time1 = $time1")
	println("time2 = $time2")
}