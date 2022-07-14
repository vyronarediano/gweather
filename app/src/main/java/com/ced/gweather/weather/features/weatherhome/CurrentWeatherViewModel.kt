package com.ced.gweather.weather.features.weatherhome

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.ced.authentication.domain.repository.SessionRepository
import com.ced.commons.clean.rx.EmptySingleObserver
import com.ced.commons.util.log.Logger
import com.ced.gweather_core.domain.interactor.AddWeatherRecordUseCase
import com.ced.gweather_core.domain.interactor.GetCurrentWeatherUseCase
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.internal.viewmodel.BaseViewModel
import java.io.IOException
import java.util.*
import javax.inject.Inject

class CurrentWeatherViewModel
@Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val addWeatherRecordUseCase: AddWeatherRecordUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    var weather: MutableLiveData<WeatherModel> = MutableLiveData()

    var currentLocCityCountry: MutableLiveData<String> = MutableLiveData()

    init {
        addUseCases {
            add(getCurrentWeatherUseCase)
        }
    }

    fun getCurrentWeather() {
        getCurrentWeatherUseCase.execute(object : EmptySingleObserver<WeatherModel>() {
            override fun onSuccess(result: WeatherModel) {
                weather.value = result

                // Add to Cloud Firestore every fetch from OpenWeatherMap (only when user opens the app)
                if (sessionRepository.isAllowedToSave == true) {
                    result.run {
                        dateCreated = Calendar.getInstance().time
                        userId = sessionRepository.loggedInUserId

                        addWeatherRecordUseCase.execute(object :
                            EmptySingleObserver<WeatherModel>() {
                            override fun onSuccess(result: WeatherModel) {
                                Logger.i(TAG, "Added new weather record.")
                            }
                        }, params = this)
                    }
                }
            }

            override fun onError(throwable: Throwable) {
                Logger.d(TAG, "Get current weather failed: ${throwable.message}", throwable)

                handleFailure(FailedToLoadCurrentWeather(throwable))
            }
        }, params = currentLocCityCountry.value)
    }

    fun setIsAllowedToSave(isAllowed: Boolean) {
        sessionRepository.isAllowedToSave = isAllowed
    }


    fun getAddressFromLocation(lat: Double, lng: Double, context: Context): List<Address> {
        var addressList: List<Address> = ArrayList()
        try {
            val geocoder = Geocoder(context)
            addressList = geocoder.getFromLocation(lat, lng, 3)

            currentLocCityCountry.value =
                "${addressList.first().locality}, ${addressList.first().countryName}"
        } catch (e: IOException) {

            Logger.e(TAG, e.message ?: "Unable to determine address from location", e)
        }

        return addressList
    }

    companion object {
        private val TAG = CurrentWeatherViewModel::class.java.simpleName
    }
}