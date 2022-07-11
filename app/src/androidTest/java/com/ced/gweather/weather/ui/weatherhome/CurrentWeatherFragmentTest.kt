package com.ced.gweather.weather.ui.weatherhome

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ced.commons.util.DeviceManager
import com.ced.gweather.R
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class CurrentWeatherFragmentTest : TestCase() {

    private lateinit var scenario: FragmentScenario<CurrentWeatherFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testGreetingMessage() {
        assertThat(
            onView(withId(R.id.tvWeatherGreeting)).check(
                matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
        )
    }

    @Test
    fun testCurrentLocationVisible() {
        assertThat(
            onView(withId(R.id.tvWeatherCurrentLocation)).check(
                matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
        )
    }

    @Test
    fun testCurrentWeather() {
        assertThat(
            onView(withId(R.id.layoutCurrentWeather)).check(
                matches(
                    ViewMatchers.withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                    )
                )
            )
        )
    }
}