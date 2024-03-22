package com.example.weathercompose.data.mappers

import com.example.weathercompose.data.network.dto.CityDto
import com.example.weathercompose.domain.entity.City

fun CityDto.toCity(): City = City(id, name, country)

fun List<CityDto>.toListCity() : List<City> = map { it.toCity() }