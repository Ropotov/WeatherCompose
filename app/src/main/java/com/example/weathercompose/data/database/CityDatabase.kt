package com.example.weathercompose.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathercompose.data.database.entity.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {

        private const val NAME_DATA_BASE: String = "city_database"
        private var INSTANCE: CityDatabase? = null
        private val LOOK = Any()

        fun getInstance(context: Context): CityDatabase {
            INSTANCE?.let { return it }
            synchronized(LOOK) {
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = CityDatabase::class.java,
                    name = NAME_DATA_BASE
                ).build()

                INSTANCE = database
                return database
            }
        }
    }
}