package com.example.weathercompose.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathercompose.data.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM cities")
    fun getFavouriteCities(): Flow<List<CityEntity>>

    @Query("SELECT EXISTS ( SELECT * FROM cities WHERE id = :cityId LIMIT 1)")
    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(cityEntity: CityEntity)

    @Query("DELETE FROM cities WHERE id = :cityId ")
    suspend fun removeCity(cityId: Int)
}