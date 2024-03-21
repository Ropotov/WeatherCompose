package com.example.weathercompose.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [DataModule::class, PresentationModule::class]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}