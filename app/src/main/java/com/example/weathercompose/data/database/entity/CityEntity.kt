package com.example.weathercompose.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
)
