package com.ced.gweather_core.domain.service

import com.ced.gweather_core.domain.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather?&units=metric&appid={YOUR_API_HERE}")
    fun getData(
        @Query(value = "q") currentLocCityCountry: String
    ): Single<WeatherModel>
}