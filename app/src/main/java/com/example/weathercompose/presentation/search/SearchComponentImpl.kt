package com.example.weathercompose.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.presentation.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchComponentImpl @Inject constructor(
    private val componentContext: ComponentContext,
    private val searchStoreFactory: SearchStoreFactory,
    private val openReason: OpenReason,
    private val onBackClick: () -> Unit,
    private val onSaveToFavouriteClick: () -> Unit,
    private val onClickCity: (city: City) -> Unit,
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
}