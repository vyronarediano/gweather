package com.ced.commons.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.toTimeString(): String {
    val date = this
    val format = SimpleDateFormat("h:mm a")

    return format.format(date)
}


fun Date.toDateTimeString(): String {
    val date = this
    val format = SimpleDateFormat("d MMM yyy, hh:mm a")

    return format.format(date)
}