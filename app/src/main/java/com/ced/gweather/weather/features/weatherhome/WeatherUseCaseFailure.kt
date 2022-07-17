package com.ced.gweather.weather.features.weatherhome

import com.ced.commons.clean.interactor.Failure
import com.ced.commons.clean.interactor.InvalidArgumentException
import com.ced.gweather.weather.ui.UiText

abstract class WeatherUseCaseFailure(throwable: Throwable, var error: UiText? = null) :
    Failure.FeatureFailure() {

    var message: String = throwable.message.toString()
        .ifEmpty { "Sorry, we encountered an error performing the action." }

    init {
        if (throwable is InvalidArgumentException) message =
            (throwable).messages?.values?.joinToString("\n").toString()
    }

}

class FailedToLoadCurrentWeather(errorMsgStrResource: UiText, throwable: Throwable) :
    WeatherUseCaseFailure(throwable, errorMsgStrResource)

class FailedToAddWeatherRecord(errorMsgStrResource: UiText, throwable: Throwable) :
    WeatherUseCaseFailure(throwable, errorMsgStrResource)

class FailedToLoadWeatherRecords(errorMsgStrResource: UiText, throwable: Throwable) :
    WeatherUseCaseFailure(throwable, errorMsgStrResource)


