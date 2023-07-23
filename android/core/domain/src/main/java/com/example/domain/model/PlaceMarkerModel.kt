package com.example.domain.model

import android.graphics.Bitmap
import com.example.data.model.PlaceModel

data class PlaceMarkerModel(
    val place:PlaceModel,
    val markerIcon:Bitmap
)
