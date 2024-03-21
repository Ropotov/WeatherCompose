package com.example.weathercompose.data.network.api

import com.example.weathercompose.data.network.dto.CityDto
import com.example.weathercompose.data.network.dto.WeatherCurrentDto
import com.example.weathercompose.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("current.json?key=1949f63f52ee459dbe8130328242103")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json?key=1949f63f52ee459dbe8130328242103")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4
    ): WeatherForecastDto

    @GET("search.json?key=1949f63f52ee459dbe8130328242103")
    suspend fun searchCity(
        @Query("q") query: String,
    ): List<CityDto>
}