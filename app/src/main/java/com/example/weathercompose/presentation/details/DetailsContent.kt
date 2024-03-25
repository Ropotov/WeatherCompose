package com.example.weathercompose.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weathercompose.R
import com.example.weathercompose.domain.entity.Weather
import com.example.weathercompose.presentation.ui.theme.formattedFullDate
import com.example.weathercompose.presentation.ui.theme.formattedShortDayOfWeek
import com.example.weathercompose.presentation.ui.theme.tempToFormattedString

@Composable
fun DetailsContent(component: DetailsComponent) {

    val state by component.model.collectAsState()

    Column {
        WeatherDayBlock(state.forecastState)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherDayBlock(forecastState: DetailsStore.State.ForecastState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when (forecastState) {
            DetailsStore.State.ForecastState.Error -> {}
            DetailsStore.State.ForecastState.Initial -> {}
            is DetailsStore.State.ForecastState.Loaded -> {
                Spacer(modifier = Modifier.weight(1f))
                GlideImage(
                    model = forecastState.forecast.currentWeather.urlImage,
                    contentDescription = stringResource(R.string.icon_weather_content_description),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = forecastState.forecast.currentWeather.temp.tempToFormattedString(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = forecastState.forecast.currentWeather.date.formattedFullDate(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                AnimatedUpcomingWeather(forecastState.forecast.upcoming)
                Spacer(modifier = Modifier.weight(1f))
            }

            DetailsStore.State.ForecastState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}
@Composable
private fun AnimatedUpcomingWeather(upcoming: List<Weather>) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { IntOffset(0, it.height) }
        )
    ) {
        ForecastBlock(upcoming = upcoming)
    }
}

@Composable
fun ForecastBlock(upcoming: List<Weather>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            upcoming.forEach { DayWeatherBlock(weather = it) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RowScope.DayWeatherBlock(weather: Weather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .height(128.dp)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.inversePrimary)

    ) {
        GlideImage(
            model = weather.urlImage,
            contentDescription = stringResource(R.string.icon_weather_content_description),
            modifier = Modifier
                .size(20.dp)
                .weight(1f)
        )
        Text(
            text = weather.temp.tempToFormattedString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = weather.date.formattedShortDayOfWeek(),
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 8.sp),
            modifier = Modifier.weight(1f)
        )
    }
}
