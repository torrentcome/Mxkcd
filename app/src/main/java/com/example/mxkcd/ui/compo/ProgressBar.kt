package com.example.mxkcd.ui.compo

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    string : String? = null
    ) {
    string?.let { Text(it) }
    CircularProgressIndicator(
        modifier = modifier,
        strokeWidth = strokeWidth
    )
}