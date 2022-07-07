package com.ced.gweather_core.data.firestore

import com.ced.commons.util.log.Logger
import com.ced.gweather_core.data.entity.WeatherEntity
import com.ced.gweather_core.data.mapper.toDomain
import com.ced.gweather_core.data.mapper.toEntity
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.domain.repository.WeatherRepository
import com.ced.gweather_core.domain.service.WeatherApiService
import com.ced.gweather_core.internal.util.containsIgnoreCase
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class WeatherFireStoreRepository @Inject constructor() :
    WeatherRepository {

    private val weatherApiService = WeatherApiService()
    private val disposable = CompositeDisposable()

    private val fireStore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val weatherRef by lazy {
        fireStore.collection("/weather")
    }

    // Maintain in-memory cache to avoid too many DB calls
    private var weatherRecords = mutableListOf<WeatherModel>()

    override fun findWeatherRecordsByUserId(userId: String): Observable<List<WeatherModel>> {
        return Observable.create { emitter ->
            if (weatherRecords.isEmpty()) {
                queryAllFromDB(userId, emitter)
            } else {
                emitter.onNext(weatherRecords.asSequence().filter {
                    it.userId.containsIgnoreCase(userId)
                }.toList())
            }
        }
    }


    private fun queryAllFromDB(userId: String, emitter: Emitter<List<WeatherModel>>) {
        weatherRef.get().addOnSuccessListener { documents ->
            weatherRecords = documents.map { it.toObject(WeatherEntity::class.java).toDomain() }.toMutableList()

            val finalWeatherRecords = weatherRecords.asSequence().filter {
                it.userId.containsIgnoreCase(userId)
            }.toList()

            emitter.onNext(finalWeatherRecords)
        }.addOnFailureListener { exception ->
            emitter.onError(exception)
        }
    }

    override fun getDataFromApi(currentLocCityCountry: String): Single<WeatherModel> {
        return Single.create { emitter ->
            disposable.add(
                weatherApiService.getDataService(currentLocCityCountry)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {
                        override fun onSuccess(t: WeatherModel) {
                            emitter.onSuccess(t)
                        }

                        override fun onError(e: Throwable) {
                            emitter.onError(e)
                        }
                    })
            )
        }
    }

    override fun add(weather: WeatherModel): Single<WeatherModel> {
        return Single.create { emitter ->
            val entity = weather.toEntity()

            weatherRef.add(entity)
                .addOnCompleteListener {
                    Logger.d(
                        TAG,
                        "Successfully added new weather record: '${weather.id}'"
                    )
                }
                .addOnFailureListener {
                    Logger.w(
                        TAG,
                        "Error while adding weather with id: '${weather.id}'",
                        it
                    )
                }

            emitter.onSuccess(weather)
        }
    }

    override fun clearCache(): Completable {
        weatherRecords.clear()

        return Completable.complete()
    }


    companion object {
        private val TAG = WeatherFireStoreRepository::class.java.simpleName
    }
}