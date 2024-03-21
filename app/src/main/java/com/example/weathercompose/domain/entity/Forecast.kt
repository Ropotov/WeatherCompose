package com.example.weathercompose.domain.entity

data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>,
)
