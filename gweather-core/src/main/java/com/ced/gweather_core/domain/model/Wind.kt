package com.ced.gweather_core.domain.model


import com.google.gson.annotations.SerializedName

class Wind {
    @SerializedName("deg")
    var deg: Int? = null

    @SerializedName("gust")
    var gust: Double? = null

    @SerializedName("speed")
    var speed: Double? = null
}

