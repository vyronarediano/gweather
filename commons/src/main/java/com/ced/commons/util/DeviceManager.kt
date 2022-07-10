package com.ced.commons.util

import android.content.Context
import android.location.LocationManager
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

    fun hasLocationProviderEnabled(context: Context) : Boolean {
        return isGpsLocationProviderEnabled(context) ||
                isNetworkLocationProviderEnabled(context)
    }

    fun isNetworkLocationProviderEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun isGpsLocationProviderEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
