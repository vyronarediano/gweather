package com.ced.gweather_core.data.entity

import java.util.*


class WeatherEntity {
    var base: String? = null
    var clouds: CloudsEntity? = null
    var cod: Int? = null
    var coord: CoordEntity? = null
    var dt: Int? = null
    var id: Int? = null
    var main: MainEntity? = null
    var name: String? = null
    var sys: SysEntity? = null
    var timezone: Int? = null
    var visibility: Int? = null
    var weather: WeatherLiteEntity? = null
    var wind: WindEntity? = null
    var dateCreated: Date? = null
    var userId: String? = null
}