package com.ced.gweather.weather.ui.weatherhome

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.ui.extensions.gone
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.extensions.visible
import com.ced.commons.ui.observe
import com.ced.commons.util.DeviceManager
import com.ced.commons.util.log.Logger
import com.ced.commons.util.toTimeString
import com.ced.gweather.R
import com.ced.gweather.weather.features.weatherhome.CurrentWeatherViewModel
import com.ced.gweather.weather.features.weatherhome.FailedToAddWeatherRecord
import com.ced.gweather.weather.features.weatherhome.FailedToLoadCurrentWeather
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.ced.gweather_core.domain.model.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.current_weather_fragment.*
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class CurrentWeatherFragment : BaseFragmentDI() {

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    private var fusedLocationClient: FusedLocationProviderClient? = null

    //region Overriden Methods

    override fun layoutId() = R.layout.current_weather_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appComponent.inject(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        currentWeatherViewModel = viewModel(viewModelFactory) {
            observe(weather, ::renderCurrentWeather)
            observe(currentLocCityCountry, ::renderCurrentLoc)
            observe(showLoadingState, ::showLoadingState)
            observe(showRefreshLayout, ::showRefreshLayout)
            observe(showContentLayout, ::showContentLayout)
            observe(showEmptyState, ::showEmptyState)
            observe(failure, ::handleFailure)
        }

        swipeRefreshLayout.setOnRefreshListener {
            currentWeatherViewModel.showRefreshLayout.value = true

            currentWeatherViewModel.setIsAllowedToSave(false)

            Handler(Looper.getMainLooper()).postDelayed({
                if (DeviceManager.hasLocationProviderEnabled(requireContext())) {
                    getWeatherFromLoc()
                } else {
                    currentWeatherViewModel.showRefreshLayout.value = false
                    currentWeatherViewModel.showLoadingState.value = false
                    currentWeatherViewModel.showEmptyState.value = true

                    showSnackBar(getString(R.string.unable_to_get_location))
                }
            }, 200)
        }

        if (!hasGrantedPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getWeatherFromLoc()
        }
    }

    //endregion Overriden Methods

    //region Private Methods

    private fun renderCurrentWeather(weather: WeatherModel?) {
        tvDegreeVal.text = weather?.main?.temp?.roundToInt().toString()
        tvWeatherFeelsLike.text =
            "Feels like ${weather?.main?.feelsLike?.roundToInt().toString()}°".toUpperCase()
        tvWeatherDesc.text = weather?.weather?.first()?.description?.capitalize()

        tvWeatherSunriseVal.text =
            Date(weather?.sys?.sunrise?.toLong()?.times(1000) ?: 0).toTimeString()

        tvWeatherSunsetVal.text =
            Date(weather?.sys?.sunset?.toLong()?.times(1000) ?: 0).toTimeString()

        tvWeatherWindVal.text =
            if (weather?.wind?.gust != null) weather.wind?.gust.toString() else weather?.wind?.speed?.toString()
        tvWeatherPressureVal.text = weather?.main?.pressure.toString()
        tvWeatherHumidityVal.text = weather?.main?.humidity.toString()
        tvWeatherVisibilityVal.text = "${weather?.visibility?.div(1000).toString()} km"

        val weatherIconStr = weather?.weather?.first()?.icon.orEmpty()
        setupWeatherIcons(weatherIconStr)

        tvWeatherGreeting.text = getGreetingMessage()
    }

    private fun setupWeatherIcons(weatherIconStr: String) {
        hideAllLottie()

        val isNight: Boolean = isNight()
        if (isNight) {
            if (weatherIconStr.contains("01n")) {
                lottieNightClearSkyView.visible()
            } else if (weatherIconStr.contains("04n")
                || weatherIconStr.contains("03n")
                || weatherIconStr.contains("02n")
            ) {
                lottieMoonView.visible()
            } else if (weatherIconStr.contains("09n")
                || weatherIconStr.contains("10n")
            ) {
                lottieRainyNightView.visible()
            } else if (weatherIconStr.contains("11n")) {
                lottieRainyNighThunderstormView.visible()
            } else {
                lottieMoonView.visible()
            }
        } else {
            if (weatherIconStr.contains("01d")) {
                lottieSunnyView.visible()
            } else if (weatherIconStr.contains("02d")
                || weatherIconStr.contains("03d")
                || weatherIconStr.contains("04d")
            ) {
                lottieSunnyFewCloudsView.visible()
            } else if (weatherIconStr.contains("09d")
                || weatherIconStr.contains("10d")
            ) {
                lottieSunnyRainingView.visible()
            } else if (weatherIconStr.contains("11d")) {
                lottieDayThunderstormView.visible()
            } else {
                lottieMoonView.visible()
            }
        }
    }

    private fun hideAllLottie() {
        lottieNightClearSkyView.gone()
        lottieMoonView.gone()
        lottieRainyNightView.gone()
        lottieRainyNighThunderstormView.gone()
        lottieMoonView.gone()
        lottieSunnyView.gone()
        lottieSunnyFewCloudsView.gone()
        lottieSunnyRainingView.gone()
        lottieDayThunderstormView.gone()
    }

    private fun isNight(): Boolean {
        val isNight: Boolean
        val cal = Calendar.getInstance()
        val hour = cal[Calendar.HOUR_OF_DAY]
        isNight = hour in 18..24

        return isNight
    }

    private fun getGreetingMessage(): String {
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..18 -> "Good Afternoon"
            in 19..24 -> "Good Evening"
            else -> "Hello"
        }
    }

    private fun renderCurrentLoc(currentLoc: String?) {
        tvWeatherCurrentLocation.text = currentLoc
    }

    private fun showLoadingState(show: Boolean?) {
        if (show == true) {
            layoutShimmerCurrentWeather.startShimmer()
            layoutShimmerCurrentWeather.visible()
        } else {
            layoutShimmerCurrentWeather.stopShimmer()
            layoutShimmerCurrentWeather.gone()
        }
    }

    private fun showRefreshLayout(show: Boolean?) {
        swipeRefreshLayout.isRefreshing = show == true
    }

    private fun showContentLayout(show: Boolean?) {
        if (show == true) layoutCurrentWeather.visible() else layoutCurrentWeather.gone()
    }

    private fun showEmptyState(show: Boolean?) {
        if (show == true) layoutEmptyStateWeather.visible() else layoutEmptyStateWeather.gone()
    }

    private fun getWeatherFromLoc() {
        fusedLocationClient?.lastLocation
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    val lat = task.result.latitude
                    val long = task.result.longitude
                    getAddressFromLocation(lat, long, requireContext())

                    currentWeatherViewModel.getCurrentWeather()
                }
            }
            ?.addOnFailureListener {
                Logger.e(TAG, "Error encountered on getting location:", it)

                currentWeatherViewModel.showContentLayout.value = false
                currentWeatherViewModel.showRefreshLayout.value = false
                currentWeatherViewModel.showLoadingState.value = false
                currentWeatherViewModel.showEmptyState.value = true

                showMessage(getString(R.string.unable_to_get_location))

                currentWeatherViewModel.currentLocCityCountry.value = "Manila, Philippines"
            }
    }

    private fun getAddressFromLocation(lat: Double, lng: Double, context: Context): List<Address> {
        var addressList: List<Address> = ArrayList()
        try {
            val geocoder = Geocoder(context)
            addressList = geocoder.getFromLocation(lat, lng, 3)

            currentWeatherViewModel.currentLocCityCountry.value =
                "${addressList.first().locality}, ${addressList.first().countryName}"
        } catch (e: IOException) {

            Logger.e(TAG, e.message ?: "Unable to determine address from location:", e)
        }

        return addressList
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    private fun hasGrantedPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireContext(),
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

        currentWeatherViewModel.showRefreshLayout.value = false
        currentWeatherViewModel.showLoadingState.value = false
        currentWeatherViewModel.showContentLayout.value = false
        currentWeatherViewModel.showEmptyState.value = true
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is FailedToLoadCurrentWeather -> {
                Snackbar.make(
                    requireView(),
                    failure.error?.asString(requireContext()).orEmpty(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            is FailedToAddWeatherRecord -> {
                Snackbar.make(
                    requireView(),
                    failure.error?.asString(requireContext()).orEmpty(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> showFailureDialog()
        }
    }

    //endregion Private Methods

    companion object {

        private val TAG = CurrentWeatherFragment::class.java.simpleName

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        @JvmStatic
        fun newInstance() = CurrentWeatherFragment()
    }


}