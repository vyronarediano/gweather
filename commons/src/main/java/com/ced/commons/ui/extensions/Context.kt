package com.ced.commons.ui.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager

inline fun <reified T : Any> Context.getSystemService(): T? {
    return when (T::class.java) {
        InputMethodManager::class.java -> this.getSystemService(Context.INPUT_METHOD_SERVICE) as T
        Vibrator::class.java -> this.getSystemService(Context.VIBRATOR_SERVICE) as T
        ConnectivityManager::class.java -> this.getSystemService(Context.CONNECTIVITY_SERVICE) as T
        else -> null
    }
}