package com.ced.authentication.data.mapper

import com.ced.authentication.data.entity.UserEntity
import com.ced.authentication.domain.model.User

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun User.toEntity(): UserEntity {
    val domain = this
    return UserEntity().apply {
        email = domain.email
        id = domain.id
        mobileNumber = domain.mobileNumber
        name = domain.name
        dateCreated = domain.dateCreated
    }
}

fun UserEntity.toDomain(): User {
    val entity = this
    check(entity.id != null) { "User id is null" }

    return User(entity.id!!).apply {
        email = entity.email
        mobileNumber = entity.mobileNumber
        name = entity.name
        dateCreated = entity.dateCreated
    }
}