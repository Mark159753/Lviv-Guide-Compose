package com.example.map.ui.ar.helpers

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil.ImageLoader
import com.example.core.common.BuildConfig
import com.example.data.model.PlaceModel
import com.example.domain.untils.getMarkerImageLoader
import com.example.domain.usecases.LoadImageUseCase
import com.example.map.R
import com.google.ar.sceneform.rendering.ViewRenderable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GenerateViewRenders @Inject constructor(
    private val imageLoader: LoadImageUseCase,
    @ApplicationContext
    private val context:Context
) {

    private val markerLoader: ImageLoader by lazy { getMarkerImageLoader(context) }

    suspend operator fun invoke(
        places:List<PlaceModel>,
        colors: List<Color>,
    ) = withContext(Dispatchers.Main){
        places.map { placeItem ->
            async {
                createViewRenderable()?.let { viewRenderable ->
                    val root = viewRenderable.view.findViewById<FrameLayout>(R.id.root)
                    val imageView = viewRenderable.view.findViewById<ImageView>(R.id.image)
                    val title = viewRenderable.view.findViewById<TextView>(R.id.title)
                    val rating = viewRenderable.view.findViewById<TextView>(R.id.rating)

                    val imageDrawable = imageLoader(
                        url = BuildConfig.BASE_URL + placeItem.headImage,
                        loader = markerLoader
                    )

                    imageView.setImageDrawable(imageDrawable)

                    root.setBackgroundColor(colors.random().toArgb())
                    title.text = placeItem.title
                    rating.text = placeItem.rating.toString()

                    ViewRenderData(
                        id = placeItem.id,
                        lat = placeItem.lat,
                        lon = placeItem.lon,
                        viewRender = viewRenderable
                    )
                }
            }
        }.awaitAll()
            .filterNotNull()
    }

    private suspend fun createViewRenderable() = suspendCoroutine { continuation ->
        ViewRenderable.builder()
            .setView(context, R.layout.ar_place_node)
            .build()
            .handle { viewRenderable, throwable ->
                if (throwable == null){
                    continuation.resume(viewRenderable)
                }else{
                    Log.e("ViewRenderable", throwable.stackTraceToString())
                    continuation.resume(null)
                }
            }
    }
}

data class ViewRenderData(
    val id:Int,
    val lat:Double,
    val lon:Double,
    val viewRender:ViewRenderable
)