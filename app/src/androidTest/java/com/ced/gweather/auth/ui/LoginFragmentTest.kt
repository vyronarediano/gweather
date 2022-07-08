package com.ced.gweather.auth.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ced.gweather.R
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginFragmentTest : TestCase() {

    private lateinit var scenario: FragmentScenario<LoginFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testLoginWithEmailAndPassEmpty() {
        onView(withId(R.id.tfLoginEmail)).perform(typeText(""))
        onView(withId(R.id.tfLoginPass)).perform(typeText(""))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnLoginSubmit)).perform(click())

        assertThat(onView(withId(R.id.tvLoginErrorMessage)).check(matches(withText("Please provide both email and password."))))
    }

    @Test
    fun testLoginWithInvalidPass() {
        onView(withId(R.id.tfLoginEmail)).perform(typeText("ced@gmail.com"))
        onView(withId(R.id.tfLoginPass)).perform(typeText("invalidpass"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnLoginSubmit)).perform(click())

        assertThat(onView(withId(R.id.tvLoginErrorMessage)).check(matches(withText("Unable to login. Please check your email address or password."))))
    }
}