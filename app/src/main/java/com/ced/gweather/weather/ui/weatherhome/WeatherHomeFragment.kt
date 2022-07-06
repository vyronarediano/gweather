package com.ced.gweather.weather.ui.weatherhome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ced.authentication.domain.model.User
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.observe
import com.ced.gweather.R
import com.ced.gweather.commons.ui.BaseFragment
import com.ced.gweather.weather.features.weatherhome.WeatherHomeViewModel
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.weather_home_tabbed_fragment.*

class WeatherHomeFragment: BaseFragmentDI() {

    private lateinit var weatherHomeViewModel: WeatherHomeViewModel

    override fun layoutId() = R.layout.weather_home_tabbed_fragment

    private var currentFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        weatherHomeViewModel = viewModel(viewModelFactory) {
            observe(user, ::onFinishLoadUser)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle("GWeather")

        weatherHomeViewModel.loadLoggedInUser()

        setupTabs()
    }

    private fun setupTabs() {
        pager.adapter = WeatherHomeTabAdapter(this)

        TabLayoutMediator(tabLayoutContainer, pager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun onFinishLoadUser(user: User?) {
        tvWeatherHomeName.text = "Hello, ${user?.name}!"
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed()
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