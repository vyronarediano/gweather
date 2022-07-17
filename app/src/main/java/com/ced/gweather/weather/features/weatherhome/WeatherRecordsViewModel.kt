package com.ced.gweather.weather.features.weatherhome

import androidx.lifecycle.MutableLiveData
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.clean.rx.EmptyObserver
import com.ced.commons.util.log.Logger
import com.ced.gweather_core.domain.interactor.GetWeatherRecordsUseCase
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.internal.viewmodel.BaseViewModel
import javax.inject.Inject

class WeatherRecordsViewModel
@Inject constructor(
    private val getWeatherRecordsUseCase: GetWeatherRecordsUseCase,
) : BaseViewModel() {

    var weatherRecords: MutableLiveData<List<WeatherModel>> = MutableLiveData()

    var showLoadingState = MutableLiveData(false)

    init {
        addUseCases {
            add(getWeatherRecordsUseCase)
        }
    }

    fun loadWeatherRecords() {
        Logger.d(TAG, "Loading weather records...")

        showLoadingState.value = true

        getWeatherRecordsUseCase.execute(object : EmptyObserver<List<WeatherModel>>() {
            override fun onNext(t: List<WeatherModel>) {
                weatherRecords.value = t

                showLoadingState.value = false
            }

            override fun onError(e: Throwable) {
                Logger.e(TAG, "Weather records loading failed: ${e.message}", e)

                showLoadingState.value = false

                handleFailure(Failure.ServerError)
            }
        })
    }


    companion object {
        private val TAG = WeatherRecordsViewModel::class.java.simpleName
    }
}