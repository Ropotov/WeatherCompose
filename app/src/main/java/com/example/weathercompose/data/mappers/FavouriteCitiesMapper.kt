package com.example.weathercompose.data.mappers

import com.example.weathercompose.data.database.entity.CityEntity
import com.example.weathercompose.domain.entity.City

fun City.toDbEntity(): CityEntity = CityEntity(id, name, country)

fun CityEntity.toCity(): City = City(id, name, country)

fun List<CityEntity>.toListCity(): List<City> = map { it.toCity() }