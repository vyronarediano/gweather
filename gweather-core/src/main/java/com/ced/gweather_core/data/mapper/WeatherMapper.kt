package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.WeatherEntity
import com.ced.gweather_core.domain.model.WeatherModel

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun WeatherModel.toEntity(): WeatherEntity {
    val domain = this
    return WeatherEntity().apply {
        base = domain.base
        clouds = domain.clouds?.toEntity()
        cod = domain.cod
        coord = domain.coord?.toEntity()
        dt = domain.dt
        id = domain.id
        main = domain.main?.toEntity()
        name = domain.name
        sys = domain.sys?.toEntity()
        timezone = domain.timezone
        visibility = domain.visibility
        weather = domain.weather?.first()?.toEntity()
        wind = domain.wind?.toEntity()
        dateCreated = domain.dateCreated
        userId = domain.userId

    }
}

fun WeatherEntity.toDomain(): WeatherModel {
    val entity = this
    return WeatherModel().apply {
        base = entity.base
        clouds = entity.clouds?.toDomain()
        cod = entity.cod
        coord = entity.coord?.toDomain()
        dt = entity.dt
        id = entity.id
        main = entity.main?.toDomain()
        name = entity.name
        sys = entity.sys?.toDomain()
        timezone = entity.timezone
        visibility = entity.visibility
        weather = listOf(entity.weather?.toDomain())
        wind = entity.wind?.toDomain()
        dateCreated = entity.dateCreated
        userId = entity.userId
    }
}