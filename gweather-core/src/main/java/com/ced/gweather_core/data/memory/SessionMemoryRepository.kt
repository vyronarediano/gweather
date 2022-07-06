package com.ced.gweather_core.data.memory

import com.ced.authentication.domain.model.User
import com.ced.authentication.domain.repository.SessionRepository
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class SessionMemoryRepository @Inject constructor():
    SessionRepository {

    override var loggedInUser: User? = null

    override var isAllowedToSave: Boolean? = null

}