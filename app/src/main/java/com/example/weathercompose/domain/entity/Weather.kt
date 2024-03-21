package com.example.weathercompose.domain.entity

import java.util.Calendar

data class Weather(
    val temp: Float,
    val condition: String,
    val urlImage: String,
    val date: Calendar,
)
