package com.ced.authentication.domain.repository

import com.ced.authentication.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
interface UserRepository {
    fun findByEmail(email: String) : Observable<User>

    fun findByNumber(number: String) : Observable<User>

    fun add(user: User): Single<User>
}