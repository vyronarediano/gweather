package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.MainEntity
import com.ced.gweather_core.domain.model.Main

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Main.toEntity(): MainEntity {
    val domain = this
    return MainEntity().apply {
        feelsLike = domain.feelsLike
        humidity = domain.humidity
        pressure = domain.pressure
        temp = domain.temp
        tempMax = domain.tempMax
        tempMin = domain.tempMax
    }
}

fun MainEntity.toDomain(): Main {
    val entity = this
    return Main().apply {
        feelsLike = entity.feelsLike
        humidity = entity.humidity
        pressure = entity.pressure
        temp = entity.temp
        tempMax = entity.tempMax
        tempMin = entity.tempMax
    }
}