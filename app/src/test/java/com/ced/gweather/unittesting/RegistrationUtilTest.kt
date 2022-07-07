package com.ced.gweather.unittesting

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty name returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "ced@gmail.com",
            "qweqwe"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty emailAddress returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "ced",
            "",
            "qweqwe"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "ced",
            "ced@gmail.com",
            ""
        )
        assertThat(result).isFalse()
    }


    @Test
    fun `email address already exists returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "ced",
            "ced@gmail.com",
            "asdasd"
        )
        assertThat(result).isFalse()
    }

}