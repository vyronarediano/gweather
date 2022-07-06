package com.ced.gweather.auth.domain.model

import android.content.Context
import javax.inject.Inject

class WeatherAuthenticationValues @Inject constructor(context: Context) : AuthenticationValues {

    private val PREFERENCES_APP_ID = "com.ced.gweather"
    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES_APP_ID, Context.MODE_PRIVATE)

    private val KEY_LOGGED_IN_EMAIL = "GWEATHER_PREFERENCES_EMAIL"
    private val KEY_PIN = "GWEATHER_PREFERENCES_PIN"
    private val KEY_DEVICE_ID = "GWEATHER_DEVICE_ID"

    override var loggedInEmail: String?
        get() = sharedPreferences.getString(KEY_LOGGED_IN_EMAIL, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_LOGGED_IN_EMAIL, value).apply()
        }

    override var deviceId: String?
        get() = sharedPreferences.getString(KEY_DEVICE_ID, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_DEVICE_ID, value).apply()
        }

    override fun clear() {
        // we need to persist this even after logout
        val savedDeviceId = deviceId

        sharedPreferences.edit().clear().apply()

        deviceId = savedDeviceId
    }

}