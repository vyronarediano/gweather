package com.ced.authentication.domain.repository

import com.ced.authentication.domain.model.User

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
interface SessionRepository {

    var loggedInUser: User?

    var isAllowedToSave: Boolean?

}