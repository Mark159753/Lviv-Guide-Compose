package com.example.domain.usecases

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.core.common.BuildConfig
import com.example.core.common.di.MainDispatcher
import com.example.data.model.PlaceModel
import com.example.domain.R
import com.example.domain.model.PlaceMarkerModel
import com.example.domain.untils.getMarkerImageLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

val PlaceMarkerWidth = 55.dp

class CreatePlacesMarkerDataUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    @MainDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val imageLoader: LoadImageUseCase
) {

    private val markerLoader:ImageLoader by lazy { getMarkerImageLoader(context) }

    suspend operator fun invoke(
        places:List<PlaceModel>,
        colors: List<Color>
    ) = withContext(dispatcher){
        val markerWidth = with(Density(context)){ PlaceMarkerWidth.toPx().roundToInt() }

        places.map { placeItem ->
            async {
                val imageDrawable = imageLoader(
                    url = BuildConfig.BASE_URL + placeItem.headImage,
                    size = markerWidth,
                    loader = markerLoader
                )

                val markerIcon = createMarkerIcon(
                    drawable = imageDrawable,
                    color = colors.random(),
                    width = markerWidth
                )

                PlaceMarkerModel(
                    place = placeItem,
                    markerIcon = markerIcon
                )
            }
        }.awaitAll()
    }

    private fun createMarkerIcon(
        drawable: Drawable?,
        color: Color,
        width: Int = 0,
    ): Bitmap {
        val view = LayoutInflater.from(context).inflate(R.layout.marker_view, null)
        val imageView = view.findViewById<ImageView>(R.id.marker_image)

        view.setBackgroundResource(R.drawable.marker_icon)
        view.backgroundTintList = ColorStateList.valueOf(color.toArgb())

        drawable?.let { image ->
            imageView.setImageDrawable(image)
        }

        val ratio = 1.16f

        val h = (width * ratio).roundToInt()

        if (width > 0 && h > 0) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    width, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    h, View.MeasureSpec.EXACTLY
                )
            )
        }
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background = view.background
        background?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }
}