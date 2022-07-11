package com.ced.gweather.weather.ui.weatherhome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.ui.extensions.gone
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.extensions.visible
import com.ced.commons.ui.observe
import com.ced.gweather.R
import com.ced.gweather.weather.features.weatherhome.FailedToLoadWeatherRecords
import com.ced.gweather.weather.features.weatherhome.WeatherRecordsViewModel
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.ced.gweather_core.domain.model.WeatherModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.weather_records_fragment.*

class WeatherRecordsFragment : BaseFragmentDI() {

    private lateinit var weatherRecordsViewModel: WeatherRecordsViewModel
    private lateinit var weatherRecordsAdapter: WeatherRecordsAdapter

    override fun layoutId(): Int = R.layout.weather_records_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        weatherRecordsViewModel = viewModel(viewModelFactory) {
            observe(weatherRecords, ::renderWeatherRecords)
            observe(failure, ::handleFailure)
        }

        weatherRecordsAdapter = WeatherRecordsAdapter(weatherRecordsViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvWeatherRecords.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = weatherRecordsAdapter
        }

        weatherRecordsViewModel.loadWeatherRecords()

        showShimmerEffect(true)

        swipeRefreshLayoutWeatherRecords.setOnRefreshListener {
            swipeRefreshLayoutWeatherRecords.isRefreshing = true
            showShimmerEffect(true)
            rvWeatherRecords.gone()

            Handler(Looper.getMainLooper()).postDelayed({
                weatherRecordsViewModel.loadWeatherRecords()
            }, 500)
        }
    }

    private fun renderWeatherRecords(weatherRecords: List<WeatherModel>?) {
        swipeRefreshLayoutWeatherRecords.isRefreshing = false
        if (weatherRecords?.size != 0) {
            showShimmerEffect(false)
            layoutEmptyStateWeatherRecordsView.gone()

            rvWeatherRecords.visible()
            weatherRecords?.let { weatherRecordsAdapter.setWeatherRecords(it) }
        } else {
            showShimmerEffect(false)
            rvWeatherRecords.gone()

            layoutEmptyStateWeatherRecordsView.visible()
        }
    }

    private fun handleFailure(failure: Failure?) {
        showShimmerEffect(false)

        when (failure) {
            is FailedToLoadWeatherRecords -> {
                Snackbar.make(
                    requireView(),
                    resources.getString(R.string.unable_to_load_weather_records),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> showFailureDialog()
        }
    }

    private fun showShimmerEffect(show: Boolean) {
        if (show) {
            layoutShimmerWeatherRecords.startShimmer()
            layoutShimmerWeatherRecords.visible()
        } else {
            layoutShimmerWeatherRecords.stopShimmer()
            layoutShimmerWeatherRecords.gone()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = WeatherRecordsFragment()
    }
}