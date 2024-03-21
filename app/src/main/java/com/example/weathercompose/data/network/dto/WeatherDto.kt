package com.example.weathercompose.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_update_epoch") val date: Long,
    @SerializedName("temp_c") val temp: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
