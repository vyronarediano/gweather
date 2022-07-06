package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.SysEntity
import com.ced.gweather_core.domain.model.Sys

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Sys.toEntity(): SysEntity {
    val domain = this
    return SysEntity().apply {
        country = domain.country
        id = domain.id
        sunrise = domain.sunrise
        sunset = domain.sunset
        type = domain.type
    }
}

fun SysEntity.toDomain(): Sys {
    val entity = this
    return Sys().apply {
        country = entity.country
        id = entity.id
        sunrise = entity.sunrise
        sunset = entity.sunset
        type = entity.type
    }
}