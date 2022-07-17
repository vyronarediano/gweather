package com.ced.gweather.weather.features.weatherhome

import androidx.lifecycle.MutableLiveData
import com.ced.authentication.domain.repository.SessionRepository
import com.ced.commons.clean.rx.EmptySingleObserver
import com.ced.commons.util.log.Logger
import com.ced.gweather.R
import com.ced.gweather.weather.ui.UiText
import com.ced.gweather_core.domain.interactor.AddWeatherRecordUseCase
import com.ced.gweather_core.domain.interactor.GetCurrentWeatherUseCase
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.internal.viewmodel.BaseViewModel
import java.util.*
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class CurrentWeatherViewModel
@Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val addWeatherRecordUseCase: AddWeatherRecordUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    var weather: MutableLiveData<WeatherModel> = MutableLiveData()

    var currentLocCityCountry: MutableLiveData<String> = MutableLiveData()

    var showLoadingState = MutableLiveData(false)
    var showRefreshLayout = MutableLiveData(false)
    var showContentLayout = MutableLiveData(false)
    var showEmptyState = MutableLiveData(false)

    init {
        addUseCases {
            add(getCurrentWeatherUseCase)
        }
    }

    fun getCurrentWeather() {
        showContentLayout.value = false
        showEmptyState.value = false
        showLoadingState.value = true

        getCurrentWeatherUseCase.execute(
            params = currentLocCityCountry.value,
            object : EmptySingleObserver<WeatherModel>() {
                override fun onSuccess(result: WeatherModel) {
                    showRefreshLayout.value = false
                    showLoadingState.value = false
                    showContentLayout.value = true

                    weather.value = result

                    addWeatherRecord(result)
                }

                override fun onError(throwable: Throwable) {
                    Logger.d(TAG, "Get current weather failed: ${throwable.message}", throwable)

                    showContentLayout.value = false
                    showRefreshLayout.value = false
                    showLoadingState.value = false
                    showEmptyState.value = true

                    handleFailure(
                        FailedToLoadCurrentWeather(
                            UiText.StringResource(restId = R.string.unable_to_get_current_weather),
                            throwable
                        )
                    )
                }

                /**
                 * Add to Cloud Firestore every fetch from OpenWeatherMap (only when user opens the app)
                 */
                private fun addWeatherRecord(result: WeatherModel) {
                    if (sessionRepository.isAllowedToSave == true) {
                        result.run {
                            dateCreated = Calendar.getInstance().time
                            userId = sessionRepository.loggedInUserId

                            addWeatherRecordUseCase.execute(params = this, object :
                                EmptySingleObserver<WeatherModel>() {
                                override fun onSuccess(result: WeatherModel) {
                                    Logger.i(TAG, "Added new weather record.")
                                }

                                override fun onError(throwable: Throwable) {
                                    Logger.e(
                                        TAG,
                                        "Error encountered while adding weather record: $throwable"
                                    )

                                    handleFailure(
                                        FailedToAddWeatherRecord(
                                            UiText.StringResource(restId = R.string.unable_to_add_weather_record),
                                            throwable
                                        )
                                    )
                                }
                            })
                        }
                    }
                }

            })
    }

    /**
     * For checking if user is allowed to add weather record to
     * Cloud Firestore every fetch from OpenWeatherMap (only when user opens the app)
     */
    fun setIsAllowedToSave(isAllowed: Boolean) {
        sessionRepository.isAllowedToSave = isAllowed
    }

    companion object {
        private val TAG = CurrentWeatherViewModel::class.java.simpleName
    }
}