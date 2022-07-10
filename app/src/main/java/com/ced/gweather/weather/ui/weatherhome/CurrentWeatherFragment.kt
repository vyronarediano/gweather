package com.ced.gweather.weather.ui.weatherhome

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.ui.extensions.gone
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.extensions.visible
import com.ced.commons.ui.observe
import com.ced.commons.util.DeviceManager
import com.ced.commons.util.log.Logger
import com.ced.gweather.R
import com.ced.gweather.weather.features.weatherhome.CurrentWeatherViewModel
import com.ced.gweather.weather.features.weatherhome.FailedToLoadCurrentWeather
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.ced.gweather_core.domain.model.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.current_weather_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CurrentWeatherFragment : BaseFragmentDI() {

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun layoutId() = R.layout.current_weather_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appComponent.inject(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        currentWeatherViewModel = viewModel(viewModelFactory) {
            observe(weather, ::renderCurrentWeather)
            observe(currentLocCityCountry, ::renderCurrentLoc)
            observe(failure, ::handleFailure)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true

            currentWeatherViewModel.setIsAllowedToSave(false)

            Handler(Looper.getMainLooper()).postDelayed({
                if (DeviceManager.hasLocationProviderEnabled(requireContext())) {
                    getCurrentWeatherOfCurrentLoc()
                } else {
                    swipeRefreshLayout.isRefreshing = false

                    showSnackBar(getString(R.string.unable_to_get_location))
                }
            }, 500)

        }

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getCurrentWeatherOfCurrentLoc()
        }
    }

    private fun renderCurrentWeather(weather: WeatherModel?) {
        lottieEmptyView.gone()
        layoutCurrentWeather.visible()
        swipeRefreshLayout.isRefreshing = false

        tvDegreeVal.text = weather?.main?.temp?.roundToInt().toString()
        tvWeatherFeelsLike.text =
            "Feels like ${weather?.main?.feelsLike?.roundToInt().toString()}°".toUpperCase()
        tvWeatherDesc.text = weather?.weather?.first()?.description?.capitalize()

        val sunrise = weather?.sys?.sunrise?.times(1000)
        tvWeatherSunriseVal.text =
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(sunrise?.toLong() ?: 0))

        val sunset = weather?.sys?.sunset?.times(1000)
        tvWeatherSunsetVal.text =
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(sunset?.toLong() ?: 0))

        tvWeatherWindVal.text =
            if (weather?.wind?.gust != null) weather.wind?.gust.toString() else weather?.wind?.speed?.toString()
        tvWeatherPressureVal.text = weather?.main?.pressure.toString()
        tvWeatherHumidityVal.text = weather?.main?.humidity.toString()
        tvWeatherVisibilityVal.text = "${weather?.visibility?.div(1000).toString()} km"

        val isNight: Boolean
        val cal = Calendar.getInstance()
        val hour = cal[Calendar.HOUR_OF_DAY]
        isNight = hour < 6 || hour > 18

        if (isNight) {
            lottieMoonView.visible()
            lottieSunnyView.gone()
        } else {
            lottieMoonView.gone()
            lottieSunnyView.visible()
        }

        Glide.with(requireActivity())
            .load("http://openweathermap.org/img/wn/" + weather?.weather?.first()?.icon + "@2x.png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .format(DecodeFormat.PREFER_ARGB_8888)
            .into(ivWeatherIcon)
    }

    private fun renderCurrentLoc(currentLoc: String?) {
        tvWeatherCurrentLocation.text = currentLoc
    }

    private fun getCurrentWeatherOfCurrentLoc() {
        fusedLocationClient?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->

            if (task.isSuccessful && task.result != null) {
                val lat = task.result.latitude
                val long = task.result.longitude
                currentWeatherViewModel.getAddressFromLocation(lat, long, requireContext())

                currentWeatherViewModel.getCurrentWeather()
            } else {
                swipeRefreshLayout.isRefreshing = false

                Logger.w(TAG, "getLastLocation:exception", task.exception)

                showMessage(getString(R.string.unable_to_get_location))

                currentWeatherViewModel.currentLocCityCountry.value = "Manila, Philippines"
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Logger.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackBar(
                "Location permission is needed for core functionality",
                "Okay",
                Snackbar.LENGTH_LONG,
                onActionCallback = {
                    startLocationPermissionRequest()
                })
        } else {
            Logger.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }

        swipeRefreshLayout.isRefreshing = false
        layoutCurrentWeather.gone()
        lottieEmptyView.visible()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is FailedToLoadCurrentWeather -> {
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

        private val TAG = CurrentWeatherFragment::class.java.simpleName

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        @JvmStatic
        fun newInstance() = CurrentWeatherFragment()
    }


}