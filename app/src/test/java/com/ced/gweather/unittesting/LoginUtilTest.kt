package com.ced.gweather.unittesting

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoginUtilTest {

    @Test
    fun `empty email address returns false`() {
        val result = LoginUtil.validateLoginInput(
            "",
            "qweqwe"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = LoginUtil.validateLoginInput(
            "ced@gmail.com",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty email add and password returns false`() {
        val result = LoginUtil.validateLoginInput(
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `invalid email add returns false`() {
        val result = LoginUtil.validateLoginInput(
            "ced@",
            ""
        )
        assertThat(result).isFalse()
    }

}