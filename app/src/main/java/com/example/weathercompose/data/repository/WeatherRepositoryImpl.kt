package com.example.weathercompose.data.repository

import com.example.weathercompose.data.mappers.toForecast
import com.example.weathercompose.data.mappers.toWeather
import com.example.weathercompose.data.network.api.ApiService
import com.example.weathercompose.domain.entity.Forecast
import com.example.weathercompose.domain.entity.Weather
import com.example.weathercompose.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX$cityId").toWeather()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecast("$PREFIX$cityId").toForecast()
    }

    companion object {
        private const val PREFIX = "id:"
    }
}