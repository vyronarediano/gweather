package com.ced.authentication.domain.repository

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
interface SessionRepository {

    /**
     * Key we need to get the current weather from OpenWeatherMap
     */
    var openWeatherMapKey: String?

    var loggedInUserId: String?

    var loggedInUserName: String?

    var loggedInEmail: String?

    /**
     * To determine the weather fetched will only be added to record/list when user opens the app only
     */
    var isAllowedToSave: Boolean?

    fun clear()

}