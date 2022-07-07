package com.ced.commons.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 *
 */
object DeviceManager {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
