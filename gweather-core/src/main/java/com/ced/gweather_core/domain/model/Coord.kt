package com.ced.gweather_core.domain.model


import com.google.gson.annotations.SerializedName

class Coord {
    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null
}