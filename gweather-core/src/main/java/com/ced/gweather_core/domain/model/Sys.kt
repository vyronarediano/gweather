package com.ced.gweather_core.domain.model


import com.google.gson.annotations.SerializedName

class Sys {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("sunrise")
    var sunrise: Int? = null

    @SerializedName("sunset")
    var sunset: Int? = null

    @SerializedName("type")
    var type: Int? = null
}

