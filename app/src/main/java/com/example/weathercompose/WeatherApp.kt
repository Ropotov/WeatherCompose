package com.example.weathercompose

import android.app.Application
import com.example.weathercompose.di.ApplicationComponent
import com.example.weathercompose.di.DaggerApplicationComponent

class WeatherApp : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}