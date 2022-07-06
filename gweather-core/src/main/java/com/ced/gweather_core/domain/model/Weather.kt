package com.ced.gweather_core.domain.model


import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("main")
    var main: String? = null
}