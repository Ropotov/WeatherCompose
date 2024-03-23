package com.example.weathercompose.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.presentation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsComponentImpl @AssistedInject constructor(
    private val detailsStoreFactory: DetailsStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("city") private val city: City,
    @Assisted("onBackClick") private val onBackClick: () -> Unit
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> onBackClick()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State>
        get() = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickChangeFavourite() {
        store.accept(DetailsStore.Intent.ClickFavourite)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("city") city: City,
            @Assisted("onBackClick") onBackClick: () -> Unit
        ): DetailsComponentImpl
    }
}