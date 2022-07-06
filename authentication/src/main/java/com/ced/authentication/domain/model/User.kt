package com.ced.authentication.domain.model

import java.util.*

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
data class User(
    var id: String? = null
) {
    var email: String? = null

    var mobileNumber: String? = null

    var name: String? = null

    var dateCreated: Date? = null
}