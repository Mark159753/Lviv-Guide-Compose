package com.example.map.ui.marker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

internal const val MarkerAspectRatio = 0.856f
internal val MarkerWidth = 160.dp

/*
* Failed to render the Compose method into Bitmap, and MarkerItem is not used.
* */

@Composable
fun MarkerItem(
    color:Color = MaterialTheme.colorScheme.primary,
    innerOffset:Dp = 4.dp,
    strokeWidth:Dp = 4.dp
){

    val strokeWidthPx = with(LocalDensity.current){ strokeWidth.toPx() }

    Box(
        modifier = Modifier
            .padding(strokeWidth)
            .width(MarkerWidth)
            .aspectRatio(MarkerAspectRatio)
            .drawWithContent {

                drawContent()

                val circleRadius = size.width / 2f
                val pointerWidthPercentage = 0.26f
                val pointerHeightPercentage = 0.22f

                val cx = circleRadius
                val cy = circleRadius
                val diameter = circleRadius * 2f

                val pw = pointerWidthPercentage * diameter
                val ph = pointerHeightPercentage * diameter

                val peekW = pw / 3f
                val halfPeek = peekW / 2f

                val theta = asin(pw / diameter)

                val pStartX = cx + circleRadius * sin(theta) - pw
                val pStartY = cy + circleRadius * cos(theta)

                val circlePath = Path().apply {
                    addCircle(circleRadius, circleRadius, circleRadius, Path.Direction.CCW)
                }

                val pointerPath = Path().apply {
                    moveTo(pStartX, pStartY)
                    lineTo(
                        circleRadius - halfPeek,
                        pStartY + ph - halfPeek
                    )

                    lineTo(
                        circleRadius + halfPeek,
                        pStartY + ph - halfPeek
                    )

                    lineTo(
                        pStartX + pw,
                        pStartY
                    )

                    addArc(
                        circleRadius - halfPeek - halfPeek * 0.07f,
                        pStartY + ph - peekW,
                        circleRadius + halfPeek + halfPeek * 0.07f,
                        pStartY + ph - halfPeek * 0.5f,
                        20f,
                        140f,
                    )

                    close()
                }

                val circlePaint = Paint().apply {
                        this.color = color.toArgb()
                        style = Paint.Style.STROKE
                        this.strokeWidth = strokeWidthPx
                        isAntiAlias = true
                    }

                val pointerPaint = Paint().apply {
                    this.color = color.toArgb()
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                
                drawIntoCanvas { canvas ->

                    canvas.nativeCanvas.apply {
                        drawPath(circlePath, circlePaint)
//                        drawLine(circleRadius, 0f, circleRadius, size.height, circlePaint)

                        clipOutPath(circlePath)

                        drawPath(pointerPath, pointerPaint)
                    }

                }

            }
            .padding(innerOffset + strokeWidth / 2)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color.Red)
        )
    }

}

@Preview
@Composable
fun MarkerItemPreview(){
    MarkerItem()
}