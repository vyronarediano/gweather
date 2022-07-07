package com.ced.gweather.unittesting

object RegistrationUtil {

    private val existingUsers = listOf("ced@gmail.com", "zed@gmail.com")

    /**
     *
     * the input is not valid if....
     *
     * ... the name/password/emailAdd is empty
     * ... the email address is already taken
     *
     */

    fun validateRegistrationInput(
        name: String,
        emailAddress: String,
        password: String
    ): Boolean {
        if (name.isEmpty() || emailAddress.isEmpty() || password.isEmpty()) {
            return false
        }
        if (emailAddress in existingUsers) {
            return false
        }
        if (name.isNotEmpty() && emailAddress.isNotEmpty() && password.isNotEmpty()) {
            return true
        }
        return true
    }
}