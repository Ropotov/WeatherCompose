package com.example.weathercompose.di

import android.content.Context
import com.example.weathercompose.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [BindsDataModule::class, ProvidesDataModule::class, PresentationModule::class]
)
@ApplicationScope
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}