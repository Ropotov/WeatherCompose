package com.example.weathercompose.presentation.search

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

class SearchComponentImpl @Inject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onSaveToFavouriteClick") private val onSaveToFavouriteClick: () -> Unit,
    @Assisted("onClickCity") private val onClickCity: (city: City) -> Unit,
) : SearchComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State>
        get() = store.stateFlow

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> onBackClick()
                    is SearchStore.Label.ClickCity -> onClickCity(it.city)
                    SearchStore.Label.SaveToFavourite -> onSaveToFavouriteClick()
                }
            }
        }
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onSearchClick() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onCityClick(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onSaveToFavouriteClick") onSaveToFavouriteClick: () -> Unit,
            @Assisted("onClickCity") onClickCity: (city: City) -> Unit,
        ): SearchComponentImpl
    }
}