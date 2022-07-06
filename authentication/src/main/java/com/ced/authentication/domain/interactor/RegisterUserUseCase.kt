package com.ced.authentication.domain.interactor

import com.ced.authentication.domain.model.User
import com.ced.authentication.domain.repository.UserRepository
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.clean.interactor.InvalidArgumentException
import com.ced.commons.clean.interactor.SingleUseCase
import io.reactivex.Single
import io.reactivex.Single.error
import javax.inject.Inject

/**
 * Register new user
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class RegisterUserUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private var userRepository: UserRepository
) : SingleUseCase<User, User>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(params: User?): Single<User> {
        if (params == null) {
            return error(InvalidArgumentException("User is null"))
        }

        return createUser(params)
    }

    private fun createUser(params: User): Single<User> {
        return userRepository.add(params)
    }
}