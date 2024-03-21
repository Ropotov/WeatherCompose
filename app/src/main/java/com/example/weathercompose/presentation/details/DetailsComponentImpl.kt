package com.example.weathercompose.presentation.details

import com.arkivanov.decompose.ComponentContext

class DetailsComponentImpl(
    private val componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext