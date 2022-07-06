package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.WeatherLiteEntity
import com.ced.gweather_core.domain.model.Weather

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Weather.toEntity(): WeatherLiteEntity {
    val domain = this
    return WeatherLiteEntity().apply {
        description = domain.description
        icon = domain.icon
        id = domain.id
        main = domain.main
    }
}

fun WeatherLiteEntity.toDomain(): Weather {
    val entity = this
    return Weather().apply {
        description = entity.description
        icon = entity.icon
        id = entity.id
        main = entity.main
    }
}