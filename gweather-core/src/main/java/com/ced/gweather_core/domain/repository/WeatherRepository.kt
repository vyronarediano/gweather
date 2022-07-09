package com.ced.gweather_core.domain.repository

import com.ced.gweather_core.domain.model.WeatherModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
interface WeatherRepository {

    /**
     * Loads all weather records in the database
     */
    fun findWeatherRecordsByUserId(userId: String): Observable<List<WeatherModel>>

    /**
     * Gets data from OpenWeatherMap
     */
    fun getDataFromApi(currentLocCityCountry: String, apiKey: String): Single<WeatherModel>

    /**
     * Saves weather records to Firestore DB
     */
    fun add(weather: WeatherModel): Single<WeatherModel>

    fun clearCache(): Completable
}