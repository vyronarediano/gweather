package com.ced.gweather.weather.features.weatherhome

import androidx.lifecycle.MutableLiveData
import com.ced.authentication.domain.interactor.GetLoggedInUserUseCase
import com.ced.authentication.domain.model.User
import com.ced.commons.clean.rx.EmptyObserver
import com.ced.commons.util.log.Logger
import com.ced.gweather_core.internal.viewmodel.BaseViewModel
import javax.inject.Inject

class WeatherHomeViewModel
@Inject constructor(
    private val loadLoggedInUserUseCase: GetLoggedInUserUseCase
) : BaseViewModel() {

    var user = MutableLiveData<User>()

    init {
        addUseCases {
            add(loadLoggedInUserUseCase)
        }
    }

    fun loadLoggedInUser() {
        loadLoggedInUserUseCase.execute(object : EmptyObserver<User>() {
            override fun onNext(t: User) {
                user.value = t
            }

            override fun onError(e: Throwable) {
                Logger.d(TAG, "Load logged-in user failed: ${e.message}", e)
            }
        })
    }

    companion object {
        private val TAG = WeatherHomeViewModel::class.java.simpleName
    }
}