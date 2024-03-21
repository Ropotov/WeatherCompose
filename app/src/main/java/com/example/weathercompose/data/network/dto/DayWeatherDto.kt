package com.example.weathercompose.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val temp: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
