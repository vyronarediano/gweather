package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.CloudsEntity
import com.ced.gweather_core.domain.model.Clouds

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Clouds.toEntity(): CloudsEntity {
    val domain = this
    return CloudsEntity().apply {
        all = domain.all
    }
}

fun CloudsEntity.toDomain(): Clouds {
    val entity = this
    return Clouds().apply {
        all = entity.all
    }
}