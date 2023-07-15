package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.bgColors
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import kotlin.random.Random

@Composable
fun LoadingRow(
    modifier: Modifier = Modifier
){
    Spacer(
        modifier = modifier
            .placeholder(
                visible = true,
                color = bgColors.random(),
                highlight = PlaceholderHighlight.fade(),
                shape = RoundedCornerShape(30),
            )
    )
}

@Composable
fun LoadingTextBlock(
    modifier: Modifier = Modifier,
    rows: Int = 5,
    rowHeight: Dp = 40.dp,
    rowSpace:Dp = 8.dp
){
    Column(
        modifier = modifier
    ) {
        repeat(rows){ order ->
            LoadingRow(
                modifier = Modifier
                    .height(rowHeight)
                    .fillMaxWidth(getRandomFraction())
            )
            if (order != (rows - 1)){
                Spacer(modifier = Modifier.height(rowSpace))
            }
        }
    }
}

private fun getRandomFraction():Float{
    val random = Random.Default
    return random.nextFloat() * (1f - 0.6f) + 0.6f
}

@Preview
@Composable
private fun BlockPreview(){
    LoadingTextBlock(
        modifier = Modifier.fillMaxWidth()
    )
}