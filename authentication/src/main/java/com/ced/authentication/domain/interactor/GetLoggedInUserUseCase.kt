package com.ced.authentication.domain.interactor

import com.ced.authentication.domain.model.User
import com.ced.authentication.domain.repository.SessionRepository
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.clean.interactor.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Loads info of currently logged-in user
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class GetLoggedInUserUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private var sessionRepository: SessionRepository
) : ObservableUseCase<User, Unit>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Unit?): Observable<User> {
        return if (sessionRepository.loggedInUser != null) Observable.just(sessionRepository.loggedInUser) else Observable.error(
            IllegalArgumentException("User not yet logged in")
        )
    }
}