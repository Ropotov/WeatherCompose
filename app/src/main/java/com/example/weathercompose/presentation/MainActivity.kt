package com.example.weathercompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.example.weathercompose.WeatherApp
import com.example.weathercompose.presentation.root.RootComponentImpl
import com.example.weathercompose.presentation.root.RootContent
import com.example.weathercompose.presentation.ui.theme.WeatherComposeTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory
    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as WeatherApp).applicationComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                RootContent(component = rootComponentFactory.create(defaultComponentContext()))
            }
        }
    }
}