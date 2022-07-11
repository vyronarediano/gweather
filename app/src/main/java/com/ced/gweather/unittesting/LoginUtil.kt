package com.ced.gweather.unittesting

import java.util.regex.Pattern

object LoginUtil {

    private val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    /**
     *
     * the input is not valid if....
     *
     * ... password/emailAdd is empty
     * ... email is not formatted
     * ... incorrect password
     *
     */

    fun validateLoginInput(
        emailAddress: String,
        password: String
    ): Boolean {
        if (emailAddress.isEmpty() || password.isEmpty()) {
            return false
        }
        if (!isEmail(emailAddress)) {
            return false
        }
        return true
    }

    private fun isEmail(emailAdd: String): Boolean {
        return emailAddressPattern.matcher(emailAdd).matches();
    }
}