package com.ced.gweather_core.internal.util

import android.annotation.SuppressLint

object StringUtils {

}

@SuppressLint("DefaultLocale")
fun String?.containsIgnoreCase(stringToCompare: String?): Boolean {
    if (this == null || stringToCompare == null) return false

    return this.toUpperCase().contains(stringToCompare.toUpperCase())
}


