package com.example.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.example.data.model.WeatherModel
import kotlin.math.roundToInt

@Composable
fun WeatherWidget(
    weather:WeatherModel,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(weather.icon, imageLoader = context.imageLoader),
                contentDescription = weather.description,
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer {
                        scaleY = 1.26f
                        scaleX = 1.26f
                    },
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = weather.temp.roundToInt().toString() + "\u2103",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = weather.description.capitalize(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun WeatherWidgetPreview(){
    WeatherWidget(
        weather = WeatherModel(
            id = 0,
            description = "few clouds",
            icon= "https://openweathermap.org/img/wn/02d@4x.png, temp=17.56",
            temp = 17.56
        )
    )
}