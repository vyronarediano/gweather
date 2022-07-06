package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.WindEntity
import com.ced.gweather_core.domain.model.Wind

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Wind.toEntity(): WindEntity {
    val domain = this
    return WindEntity().apply {
        deg = domain.deg
        gust = domain.gust
        speed = domain.speed
    }
}

fun WindEntity.toDomain(): Wind {
    val entity = this
    return Wind().apply {
        deg = entity.deg
        gust = entity.gust
        speed = entity.speed
    }
}