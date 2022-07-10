package com.ced.gweather.weather.ui.weatherhome

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ced.gweather.R
import com.ced.gweather.commons.ui.BaseFragment
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.weather_home_tabbed_fragment.*

class WeatherHomeFragment : BaseFragmentDI() {

    override fun layoutId() = R.layout.weather_home_tabbed_fragment

    private var currentFragment: BaseFragment? = null

    private var menuItems: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle("GWeather")

        setupTabs()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_weather_home, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menuItems = menu

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Currently in Night mode
            val lightMode = menu.findItem(R.id.action_light_mode)
            lightMode.isVisible = true
        } else {
            // Currently in Light mode
            val nightMode = menu.findItem(R.id.action_night_mode)
            nightMode.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val nightModeMenuItem: MenuItem = menuItems?.findItem(R.id.action_night_mode)!!
        val lightModeMenuItem: MenuItem = menuItems?.findItem(R.id.action_light_mode)!!

        if (item.itemId == R.id.action_night_mode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            item.isVisible = false
            lightModeMenuItem.isVisible = true
        }
        if (item.itemId == R.id.action_light_mode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            item.isVisible = false
            nightModeMenuItem.isVisible = true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupTabs() {
        pager.adapter = WeatherHomeTabAdapter(this)

        TabLayoutMediator(tabLayoutContainer, pager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    class WeatherHomeTabAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        private var fragments: List<Fragment> = listOf(
            CurrentWeatherFragment.newInstance(),
            WeatherRecordsFragment.newInstance()
        )

        override fun getItemCount(): Int = TAB_TITLES.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }

    companion object {
        private val TAB_TITLES = arrayOf("Current Weather", "Activity")

        @JvmStatic
        fun newInstance() = WeatherHomeFragment()
    }
}