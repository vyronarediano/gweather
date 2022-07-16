package com.ced.gweather.weather.ui.weatherhome

import android.os.Bundle
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
            observe(showLoadingState, ::showLoading)
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

        swipeRefreshLayoutWeatherRecords.setOnRefreshListener {
            swipeRefreshLayoutWeatherRecords.isRefreshing = true

            weatherRecordsViewModel.loadWeatherRecords()
        }

        weatherRecordsViewModel.loadWeatherRecords()
    }

    private fun renderWeatherRecords(weatherRecords: List<WeatherModel>?) {
        swipeRefreshLayoutWeatherRecords.isRefreshing = false

        if (weatherRecords?.size != 0) {
            layoutEmptyStateWeatherRecordsView.gone()

            weatherRecords?.let { weatherRecordsAdapter.setWeatherRecords(it) }
        } else {
            layoutEmptyStateWeatherRecordsView.visible()
        }
    }

    private fun showLoading(show: Boolean?) {
        if (show == true) {
            layoutShimmerWeatherRecords.startShimmer()
            layoutShimmerWeatherRecords.visible()
        } else {
            layoutShimmerWeatherRecords.stopShimmer()
            layoutShimmerWeatherRecords.gone()
        }
    }

    private fun handleFailure(failure: Failure?) {
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

    companion object {

        @JvmStatic
        fun newInstance() = WeatherRecordsFragment()
    }
}