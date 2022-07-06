package com.ced.gweather_core.domain.interactor

import com.ced.authentication.domain.repository.SessionRepository
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.clean.interactor.ObservableUseCase
import com.ced.gweather_core.domain.model.WeatherModel
import com.ced.gweather_core.domain.repository.WeatherRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class GetWeatherRecordsUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private var weatherRepository: WeatherRepository,
    private var sessionRepository: SessionRepository
) : ObservableUseCase<List<WeatherModel>, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<List<WeatherModel>> {
        return weatherRepository.findWeatherRecordsByUserId(sessionRepository.loggedInUser?.id.orEmpty())
    }

}