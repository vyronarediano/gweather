package com.ced.gweather_core.data.mapper

import com.ced.gweather_core.data.entity.CoordEntity
import com.ced.gweather_core.domain.model.Coord

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Coord.toEntity(): CoordEntity {
    val domain = this
    return CoordEntity().apply {
        lat = domain.lat
        lon = domain.lon
    }
}

fun CoordEntity.toDomain(): Coord {
    val entity = this
    return Coord().apply {
        lat = entity.lat
        lon = entity.lon
    }
}