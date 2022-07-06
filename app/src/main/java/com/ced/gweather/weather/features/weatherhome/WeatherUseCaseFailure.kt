package com.ced.gweather.weather.features.weatherhome

import com.ced.commons.clean.interactor.Failure
import com.ced.commons.clean.interactor.InvalidArgumentException

abstract class WeatherUseCaseFailure(var feature: String, throwable: Throwable) :
    Failure.FeatureFailure() {

    var message: String = "Sorry, we encountered an error performing the action."

    init {
        if (throwable is InvalidArgumentException) message =
            (throwable).messages?.values?.joinToString("\n").toString()
    }

}

class FailedToLoadCurrentWeather(throwable: Throwable) :
    WeatherUseCaseFailure("Load Current Weather Failed", throwable)

class FailedToLoadWeatherRecords(throwable: Throwable) :
    WeatherUseCaseFailure("Weather Records Load Failed", throwable)


