package com.ced.commons.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

val MILLIS_IN_A_DAY = 86400000.0

fun Date.toSimpleString(): String {

    val date = this
    val nowDateAsCalendar = Calendar.getInstance()
    val dateAsCalendar = Calendar.getInstance().apply {
        this.time = date
    }

    val year = dateAsCalendar.get(Calendar.YEAR)
    val month = dateAsCalendar.get(Calendar.MONTH)
    val day = dateAsCalendar.get(Calendar.DAY_OF_MONTH)

    val format = run {

        if (year == nowDateAsCalendar.get(Calendar.YEAR) &&
            month == nowDateAsCalendar.get(Calendar.MONTH) &&
            day == nowDateAsCalendar.get(Calendar.DAY_OF_MONTH)
        ) {
            SimpleDateFormat("hh:mm a")
        } else if (year == nowDateAsCalendar.get(Calendar.YEAR)) {
            SimpleDateFormat("MMM dd")
        } else {
            SimpleDateFormat("MMM dd, yyy")
        }
    }

    return format.format(date)
}


fun Date.toDateTimeString(): String {
    val date = this
    val format = SimpleDateFormat("d MMM yyy, hh:mm a")

    return format.format(date)
}


fun Date.toDateString(): String {
    val date = this
    val format = SimpleDateFormat("MMM d, yyy")

    return format.format(date)
}

fun Date?.daysBetween(toDate: Date): Int {
    if (this == null) return 0

    val stdToDate = Calendar.getInstance().apply {
        time = toDate
        set(Calendar.MILLISECOND, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }

    val stdThisDate = Calendar.getInstance().apply {
        time = this@daysBetween
        set(Calendar.MILLISECOND, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }

    //fromDate is future date
    return if (stdToDate.before(stdThisDate)) {

        val diffInMillis = stdThisDate.timeInMillis.minus(stdToDate.timeInMillis).toDouble()
        (diffInMillis / MILLIS_IN_A_DAY).roundToInt()

    } else {

        val diffInMillis = stdToDate.timeInMillis.minus(stdThisDate.timeInMillis).toDouble()
        -(diffInMillis / MILLIS_IN_A_DAY).roundToInt()

    }

}