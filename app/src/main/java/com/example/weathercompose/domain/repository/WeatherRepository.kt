package com.example.weathercompose.domain.repository

import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.entity.Forecast
import com.example.weathercompose.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForecast(cityId: Int): Forecast

}