package com.example.weatherapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.getWeather()
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrentWeather(weatherViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        CurrentWeather()
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrentWeather(
    weatherViewModel: WeatherViewModel = viewModel()
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = weatherViewModel.isRefreshing,
        onRefresh = { weatherViewModel.getWeather() }
    )

    Box(Modifier.pullRefresh(pullRefreshState)){

        ConstraintLayout ( Modifier.fillMaxSize()) {
            val (cityName, dayAndHour, image, temperature, description) = createRefs()

            Text(
                text = weatherViewModel.weather.value?.location?.name ?: "---",
                Modifier.constrainAs(cityName) {
                    top.linkTo(parent.top, margin = 100.dp)
                    centerHorizontallyTo(parent)
                },
                style = TextStyle(
                    fontSize = 30.sp
                )
            )

            Text(
                text = "Segunda 14:00",
                Modifier.constrainAs(dayAndHour) {
                    top.linkTo(cityName.bottom, margin = 5.dp)
                    centerHorizontallyTo(cityName)
                },
                style = TextStyle(
                    fontSize = 20.sp
                )
            )

            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(weatherViewModel.getLottieAnimation())
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                Modifier
                    .constrainAs(image) {
                        top.linkTo(dayAndHour.bottom, margin = 100.dp)
                        centerHorizontallyTo(parent)
                    }.size(100.dp)
            )

            Text(
                text = "22º C",
                Modifier.constrainAs(temperature) {
                    top.linkTo(image.bottom, margin = 100.dp)
                    centerHorizontallyTo(parent)
                },
                style = TextStyle(
                    fontSize = 50.sp
                )
            )

            Text(
                text = "Ensolarado",
                Modifier.constrainAs(description) {
                    top.linkTo(temperature.bottom, margin = 5.dp)
                    centerHorizontallyTo(parent)
                },
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = weatherViewModel.isRefreshing,
            state = pullRefreshState
        )
    }
}