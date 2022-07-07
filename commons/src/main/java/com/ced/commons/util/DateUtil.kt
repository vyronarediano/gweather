package com.ced.commons.util

import java.text.SimpleDateFormat
import java.util.*

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