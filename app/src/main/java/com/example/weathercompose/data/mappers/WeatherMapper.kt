package com.example.weathercompose.data.mappers

import com.example.weathercompose.data.network.dto.WeatherCurrentDto
import com.example.weathercompose.data.network.dto.WeatherDto
import com.example.weathercompose.data.network.dto.WeatherForecastDto
import com.example.weathercompose.domain.entity.Forecast
import com.example.weathercompose.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = current.toWeather()


fun WeatherDto.toWeather(): Weather = Weather(
    temp = temp,
    condition = condition.text,
    urlImage = condition.iconUrl.toCorrectImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toForecast(): Forecast = Forecast(
    currentWeather = current.toWeather(),
    upcoming = forecast.forecastDay.drop(1).map {
        val dayWeather = it.dayWeather
        Weather(
            temp = dayWeather.temp,
            condition = dayWeather.condition.text,
            urlImage = dayWeather.condition.iconUrl.toCorrectImageUrl(),
            date = it.date.toCalendar()
        )
    }
)



private fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.toCorrectImageUrl(): String = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)