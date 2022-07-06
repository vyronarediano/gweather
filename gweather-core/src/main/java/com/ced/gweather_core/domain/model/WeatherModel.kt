package com.ced.gweather_core.domain.model


import com.google.gson.annotations.SerializedName
import java.util.*

class WeatherModel {
    @SerializedName("base")
    var base: String? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("cod")
    var cod: Int? = null

    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("dt")
    var dt: Int? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("timezone")
    var timezone: Int? = null

    @SerializedName("visibility")
    var visibility: Int? = null

    @SerializedName("weather")
    var weather: List<Weather?>? = null

    @SerializedName("wind")
    var wind: Wind? = null

    var dateCreated: Date? = null

    var userId: String? = null
}