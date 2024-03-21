package com.example.weathercompose.domain.useCase

import com.example.weathercompose.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = weatherRepository.getWeather(cityId)
}