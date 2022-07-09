package com.ced.gweather_core.domain.interactor

import com.ced.authentication.domain.repository.SessionRepository
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.clean.interactor.SingleUseCase
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.domain.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class GetCurrentWeatherUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private var weatherRepository: WeatherRepository,
    private var sessionRepository: SessionRepository
) : SingleUseCase<WeatherModel, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(params: String?): Single<WeatherModel> {
        return weatherRepository.getDataFromApi(params.orEmpty(), sessionRepository.openWeatherMapKey.orEmpty())
    }
}