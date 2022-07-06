package com.ced.gweather.auth.domain.model

interface AuthenticationValues {
    var loggedInEmail: String?
    var deviceId: String?

    fun clear()
}