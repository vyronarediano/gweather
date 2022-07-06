package com.ced.authentication.domain.interactor

import com.ced.authentication.domain.model.User
import com.ced.authentication.domain.repository.SessionRepository
import com.ced.authentication.domain.repository.UserRepository
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.clean.interactor.InvalidArgumentException
import com.ced.commons.clean.interactor.SingleUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.Single.create
import io.reactivex.Single.error
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Loads the user using email or phone number.
 * Saves the user in the session
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class GetUserUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private var userRepository: UserRepository,
    private var sessionRepository: SessionRepository
) : SingleUseCase<User, String>(threadExecutor, postExecutionThread) {

    private val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun buildUseCaseSingle(params: String?): Single<User> {
        if (params == null) {
            return error(InvalidArgumentException("email or phone number is required"))
        }

        return create { emitter ->
            findUser(params).subscribe({ user ->
                sessionRepository.loggedInUser = user
                sessionRepository.isAllowedToSave = true

                emitter.onSuccess(user)
            }, { emitter.onError(it) }).also { addDisposable(it) }
        }
    }


    private fun isEmail(userName: String): Boolean {
        return emailAddressPattern.matcher(userName).matches()
    }

    private fun findUser(emailOrNumber: String): Observable<User> {
        return if (isEmail(emailOrNumber)) {
            userRepository.findByEmail(emailOrNumber)
        } else {
            userRepository.findByNumber(emailOrNumber)
        }
    }
}