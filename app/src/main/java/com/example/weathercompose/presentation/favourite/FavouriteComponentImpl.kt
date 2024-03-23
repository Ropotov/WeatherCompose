package com.example.weathercompose.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.presentation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteComponentImpl @Inject constructor(
    private val favouriteStoreFactory: FavouriteStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onCityClick") private val onCityClick: (City) -> Unit,
    @Assisted("onSearchClick") private val onSearchClick: () -> Unit,
    @Assisted("onAddToFavouriteClick") private val onAddToFavouriteClick: () -> Unit,
) : FavouriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favouriteStoreFactory.create() }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    FavouriteStore.Label.ClickAddCity -> onAddToFavouriteClick()
                    is FavouriteStore.Label.ClickCity -> onCityClick(it.city)
                    FavouriteStore.Label.ClickSearch -> onSearchClick()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State>
        get() = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavouriteStore.Intent.ClickSearch)
    }

    override fun onClickFavourite() {
        store.accept(FavouriteStore.Intent.ClickAddCity)
    }

    override fun onClickCity(city: City) {
        store.accept(FavouriteStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCityClick") onCityClick: (City) -> Unit,
            @Assisted("onSearchClick") onSearchClick: () -> Unit,
            @Assisted("onAddToFavouriteClick") onAddToFavouriteClick: () -> Unit,
        ): FavouriteComponentImpl
    }
}